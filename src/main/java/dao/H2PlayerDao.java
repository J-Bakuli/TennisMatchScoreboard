package dao;

import exception.AlreadyExistsException;
import exception.DatabaseException;
import exception.NotFoundException;
import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.entity.PlayerEntity;
import util.HibernateUtil;

import java.util.Locale;

@Slf4j
public class H2PlayerDao implements PlayerDao {
    private static final String SELECT_BY_NAME = "from PlayerEntity p where p.name = :name";
    private static final String SELECT_BY_ID = "from PlayerEntity p where p.id = :id";
    @Override
    public Player save(Player player) {
        if (player == null) {
            throw new ValidationException("Player must not be null.");
        }

        String normalizedName = normalizeName(player.getName());

        log.debug("Saving player: id={}, name={} ", player.getId(), normalizedName);

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            PlayerEntity playerEntity = toEntity(normalizedName);

            session.persist(playerEntity);
            tx.commit();

            return toPlayer(playerEntity);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            if (isDuplicate(e)) {
                throw new AlreadyExistsException("Player with name=" + normalizedName + " already exists.", e);
            }

            throw new DatabaseException("Failed to save player with name=" + normalizedName, e);
        }
    }

    @Override
    public Player findByName(String name) {
        String normalizedName = normalizeName(name);

        log.debug("Finding player by name: name={} ", normalizedName);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            PlayerEntity playerEntity = session.createQuery(SELECT_BY_NAME, PlayerEntity.class)
                    .setParameter("name", normalizedName)
                    .uniqueResult();

            if (playerEntity == null) {
                throw new NotFoundException("Player not found by name=" + normalizedName);
            }

            return toPlayer(playerEntity);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to find player by name=" + normalizedName, e);
        }
    }

    @Override
    public Player findById(Integer id) {
        if (id == null) {
            throw new ValidationException("Player's id must not be null.");
        }

        log.debug("Finding player by id: id={} ", id);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            PlayerEntity playerEntity = session.createQuery(SELECT_BY_ID, PlayerEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (playerEntity == null) {
                throw new NotFoundException("Player not found by id=" + id);
            }

            return toPlayer(playerEntity);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to find player by id=" + id, e);
        }
    }

    private boolean isDuplicate(Exception e) {
        Throwable cause = e.getCause();
        return cause != null
                && cause.getMessage() != null
                && cause.getMessage().toLowerCase().contains("unique");
    }

    private String normalizeName(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            throw new ValidationException("Player's name must not be null or blank.");
        }

        return rawName.trim().toLowerCase(Locale.ROOT);
    }

    private Player toPlayer(PlayerEntity entity) {
        return new Player(entity.getId(), entity.getName());
    }

    private PlayerEntity toEntity(String normalizedName) {
        PlayerEntity entity = new PlayerEntity();
        entity.setName(normalizedName);
        return entity;
    }
}
