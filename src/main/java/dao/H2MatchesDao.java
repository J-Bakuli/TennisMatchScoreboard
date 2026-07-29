package dao;

import dto.FinishedMatchDto;
import exception.EntityAlreadyExistsException;
import exception.DataAccessException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import mapper.FinishedMatchDtoMapper;
import mapper.H2FinishedMatchMapper;
import model.MatchState;
import model.OngoingMatch;
import org.mapstruct.factory.Mappers;
import entity.FinishedMatchEntity;
import entity.PlayerEntity;
import util.StringUtils;

import java.util.List;

@Slf4j
public class H2MatchesDao extends AbstractH2Dao implements MatchesDao {

    // Константы объявляются первыми (пишутся в самом верху) в классе.

    // В HQL запросах используется JOIN FETCH, что эквивалентно 'INNER JOIN' в SQL.
        //
        // `INNER JOIN` вернёт только те записи о матчах, у которых все связанные сущности (`player1`, `player2`)
        // гарантированно существуют в базе. Если по какой-либо причине (например, ошибка при импорте или
        // ручное вмешательство) в таблице `matches` окажется запись со значением `NULL` в колонке `player1`,
        // то такой матч будет молчаливо исключён из выборки.
        //
        // `LEFT JOIN` является более безопасным подходом:
            //  - Он вернёт все матчи, даже если у них нарушена связь с игроком.
            //  - Это позволит приложению либо упасть с `NullPointerException` (что явно укажет на проблему
                //  с целостностью данных), либо корректно обработать такую ситуацию, если она допустима.
                //  "Падать громко и рано" часто лучше, чем молча скрывать проблемы.
        //
        // Стоит заменить `JOIN FETCH` на `LEFT JOIN FETCH` для обоих игроков и победителя
        // для большей устойчивости запроса к потенциально некорректным данным.
        //
        // (см. файл "join-fetch-left-join-fetch.md" в этом же пакете)

    // Название параметра "pattern" тоже можно вынести в именованную константу

    private final FinishedMatchDtoMapper mapper = Mappers.getMapper(FinishedMatchDtoMapper.class);

    private static final String FILTER_BY_PLAYER_PATTERN_QUERY =
            "WHERE (:pattern IS NULL " +
                    "OR LOWER(p1.name) LIKE LOWER(:pattern) " +
                    "OR LOWER(p2.name) LIKE LOWER(:pattern)) ";

    private static final String COUNT_ALL_MATCHES_BY_PLAYER_NAME_QUERY =
            "SELECT COUNT (m) FROM FinishedMatchEntity m " +
                    "JOIN m.player1 p1 " +
                    "JOIN m.player2 p2 " +
                    FILTER_BY_PLAYER_PATTERN_QUERY;

    private static final String SELECT_ALL_MATCHES_BY_PLAYER_NAME_QUERY =
            "SELECT m FROM FinishedMatchEntity m " +
                    "JOIN FETCH m.player1 p1 " +
                    "JOIN FETCH m.player2 p2 " +
                    "JOIN FETCH m.winner " +
                    FILTER_BY_PLAYER_PATTERN_QUERY +
                    "ORDER BY m.finishedAt DESC";

    @Override
    public void save(OngoingMatch ongoingMatch) {
        log.debug("Saving ongoingMatch as it is finished: uuid={}, player1={}, player2={}, matchState={}", ongoingMatch.getUuid(),
                ongoingMatch.getPlayer1(), ongoingMatch.getPlayer2(), ongoingMatch.getMatchState());

        try {
            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя.
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            MatchState matchState = ongoingMatch.getMatchState();
            PlayerEntity player1 = getSession().getReference(PlayerEntity.class, ongoingMatch.getPlayer1());
            PlayerEntity player2 = getSession().getReference(PlayerEntity.class, ongoingMatch.getPlayer2());
            PlayerEntity winner = getSession().getReference(PlayerEntity.class, matchState.getWinnerPlayerId());
            FinishedMatchEntity finishedMatchEntity = H2FinishedMatchMapper.toEntity(player1, player2, winner);
            getSession().persist(finishedMatchEntity);
        } catch (PersistenceException e) {
            if (isDuplicate(e)) {
                // ConstraintViolationException не всегда означает конфликт уникальности.
                    // К тому же у несохранённого матча в этом проекте нет уникального поля.
                throw new EntityAlreadyExistsException("Finished match already exists.", e);
            }
            throw new DataAccessException("Failed to save finished match", e);
        }
    }

    @Override
    public long countAll() {
        log.debug("Counting all finished matches");
        return countByPattern(null);
    }

    @Override
    public List<FinishedMatchDto> findAllMatches(int offset, int limit) {
        log.debug("Finding all finished matches with offset {} and limit {}", offset, limit);
        return findByPattern(null, offset, limit);
    }

    @Override
    public List<FinishedMatchDto> findMatchesByPlayerName(String playerName, int offset, int limit) {
        log.debug("Finding finished matches by {} with offset {} and limit {}", playerName, offset, limit);
        String pattern = bringToPattern(playerName);
        return findByPattern(pattern, offset, limit);
    }

    @Override
    public long countByPlayerName(String playerName) {
        log.debug("Counting all finished matches by {}", playerName);
        String pattern = bringToPattern(playerName);
        return countByPattern(pattern);
    }

    private Long countByPattern(String pattern) {
        try {
            return getSession().createQuery(COUNT_ALL_MATCHES_BY_PLAYER_NAME_QUERY, Long.class)
                    .setParameter("pattern", pattern)
                    .getSingleResult();
        } catch (PersistenceException e) {
            throw new DataAccessException("Failed to count finished matches", e);
        }
    }

    private List<FinishedMatchDto> findByPattern(String pattern, int offset, int limit) {
        try {
            List<FinishedMatchEntity> matchEntities = getSession()
                    .createQuery(SELECT_ALL_MATCHES_BY_PLAYER_NAME_QUERY, FinishedMatchEntity.class)
                    .setParameter("pattern", pattern)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();

            // Преобразование "DTO <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            return mapper.toDto(matchEntities);
        } catch (PersistenceException e) {
            throw new DataAccessException("Failed to find finished matches", e);
        }
    }

    private String bringToPattern(String playerName) {
        return "%" + StringUtils.normalizeInput(playerName) + "%";
    }
}
