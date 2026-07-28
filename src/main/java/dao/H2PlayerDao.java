package dao;

import exception.EntityAlreadyExistsException;
import exception.DataAccessException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import mapper.H2PlayerMapper;
import model.Player;
import entity.PlayerEntity;
import util.StringUtils;

import java.util.Optional;

@Slf4j
public class H2PlayerDao extends AbstractH2Dao implements PlayerDao {
    // DAO должен работать с JPA Entity игрока, а не его доменной моделью
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
    private static final String SELECT_BY_NAME_QUERY =
            "from PlayerEntity p " +
            "where p.name = :name";
    private static final String SELECT_BY_ID_QUERY = "" +
            "from PlayerEntity p " +
            "where p.id = :id";

    @Override
    public Player save(Player player) {

        // Нормализация имени перед сохранением должна выполняться из сервисного слоя —
            // там, где создаётся (должен создаваться) объект JPA Entity сущности.
        String normalizedName = StringUtils.normalizeInput(player.name());

        log.debug("Saving player: id={}, name={} ", player.id(), normalizedName);

        try {

            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            PlayerEntity playerEntity = H2PlayerMapper.toEntity(normalizedName);
            getSession().persist(playerEntity);

            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            return H2PlayerMapper.toPlayer(playerEntity);
        } catch (PersistenceException e) {
            if (isDuplicate(e)) {
                // ConstraintViolationException не всегда означает конфликт уникальности.
                throw new EntityAlreadyExistsException("Player with name=" + normalizedName + " already exists.", e);
            }
            throw new DataAccessException("Failed to save player with name=" + normalizedName, e);
        }
    }

    @Override
    public Optional<Player> findByName(String name) {

        // Нормализация имени перед сохранением должна выполняться из сервисного слоя —
            // там, где создаётся (должен создаваться) объект JPA Entity сущности.
        String normalizedName = StringUtils.normalizeInput(name);

        log.debug("Finding player by name: name={} ", normalizedName);

        try {
            PlayerEntity playerEntity = getSession().createQuery(SELECT_BY_NAME_QUERY, PlayerEntity.class)
                    .setParameter("name", normalizedName)
                    .uniqueResult();
            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            return Optional.ofNullable(playerEntity).map(H2PlayerMapper::toPlayer);
        } catch (PersistenceException e) {
            throw new DataAccessException("Failed to find player by name=" + normalizedName, e);
        }
    }

    @Override
    public Optional<Player> findById(Integer id) {
        log.debug("Finding player by id: id={} ", id);

        try {
            PlayerEntity playerEntity = getSession().createQuery(SELECT_BY_ID_QUERY, PlayerEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).
                // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
            return Optional.ofNullable(playerEntity).map(H2PlayerMapper::toPlayer);
        } catch (PersistenceException e) {
            throw new DataAccessException("Failed to find player by id=" + id, e);
        }
    }
}
