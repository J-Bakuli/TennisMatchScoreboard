package dao;

import exception.AlreadyExistsException;
import exception.DataAccessException;
import exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import mapper.H2PlayerMapper;
import model.Player;
import persistence.entity.PlayerEntity;
import util.PlayerUtils;

@Slf4j
public class H2PlayerDao extends AbstractH2Dao implements PlayerDao {

    // Текст HQL запроса удобнее читать, когда он логично разбит на строки, даже если он короткий.
        // Для визуального разделения запросов на строки лучше использовать текстовые блоки

    // DAO должен работать с JPA Entity игрока, а не его доменной моделью
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)

    // Можно добавить суффикс '_HQL' или '_QUERY' к константам с текстом запросов.

    // Здесь запросы называются SELECT*, а в H2MatchesDao — FIND*. Для единообразия можно выбрать один вариант.
    private static final String SELECT_BY_NAME = "from PlayerEntity p where p.name = :name";
    private static final String SELECT_BY_ID = "from PlayerEntity p where p.id = :id";

    @Override
    public Player save(Player player) {

        // Нормализация имени перед сохранением должна выполняться из сервисного слоя —
            // там, где создаётся (должен создаваться) объект JPA Entity сущности.
        String normalizedName = PlayerUtils.normalizeInput(player.name());

        log.debug("Saving player: id={}, name={} ", player.id(), normalizedName);

        try {

            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            PlayerEntity playerEntity = H2PlayerMapper.toEntity(normalizedName);
            session().persist(playerEntity);

            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            return H2PlayerMapper.toPlayer(playerEntity);

        // TODO: Ловится слишком общее исключение. (см. файл "dao.md" в этом же пакете)
        } catch (Exception e) {
            if (isDuplicate(e)) {

                // ConstraintViolationException не всегда означает конфликт уникальности.
                throw new AlreadyExistsException("Player with name=" + normalizedName + " already exists.", e);
            }

            throw new DataAccessException("Failed to save player with name=" + normalizedName, e);
        }
    }

    @Override
    public Player findByName(String name) {

        // Нормализация имени перед сохранением должна выполняться из сервисного слоя —
            // там, где создаётся (должен создаваться) объект JPA Entity сущности.
        String normalizedName = PlayerUtils.normalizeInput(name);

        log.debug("Finding player by name: name={} ", normalizedName);

        try {
            PlayerEntity playerEntity = session().createQuery(SELECT_BY_NAME, PlayerEntity.class)
                    .setParameter("name", normalizedName)
                    .uniqueResult();

            if (playerEntity == null) {

                // Отсутствие записи в БД является нормальной и допустимой ситуацией —
                    // в этом случае не нужно выбрасывать исключение
                throw new NotFoundException("Player not found by name=" + normalizedName);
            }

            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            return H2PlayerMapper.toPlayer(playerEntity);
        } catch (NotFoundException e) {
            throw e;

        // TODO: Ловится слишком общее исключение. (см. файл "dao.md" в этом же пакете)
        } catch (Exception e) {
            throw new DataAccessException("Failed to find player by name=" + normalizedName, e);
        }
    }

    @Override
    public Player findById(Integer id) {
        log.debug("Finding player by id: id={} ", id);

        try {
            PlayerEntity playerEntity = session().createQuery(SELECT_BY_ID, PlayerEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (playerEntity == null) {

                // Отсутствие записи в БД является нормальной и допустимой ситуацией —
                    // в этом случае не нужно выбрасывать исключение
                throw new NotFoundException("Player not found by id=" + id);
            }

            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            return H2PlayerMapper.toPlayer(playerEntity);
        } catch (NotFoundException e) {
            throw e;

        // TODO: Ловится слишком общее исключение. (см. файл "dao.md" в этом же пакете)
        } catch (Exception e) {
            throw new DataAccessException("Failed to find player by id=" + id, e);
        }
    }
}
