package dao;

import exception.AlreadyExistsException;
import exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import mapper.H2FinishedMatchMapper;
import model.FinishedMatch;
import model.OngoingMatch;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.entity.FinishedMatchEntity;
import util.HibernateUtil;
import validation.MatchValidation;

import java.util.List;

@Slf4j
public class H2MatchesDao extends AbstractH2Dao implements MatchesDao {
    private static final String FIND_ALL_MATCHES =
            "SELECT m FROM FinishedMatchEntity m " +
                    "JOIN FETCH m.player1 " +
                    "JOIN FETCH m.player2 " +
                    "JOIN FETCH m.winner " +
                    "ORDER BY m.finishedAt DESC";

    @Override
    public FinishedMatch save(OngoingMatch ongoingMatch) {
        MatchValidation.validateOngoingMatch(ongoingMatch);

        log.debug("Saving ongoingMatch as it is finished: uuid={}, player1={}, player2={}, matchState={}, winner={}", ongoingMatch.getUuid(),
                ongoingMatch.getPlayer1(), ongoingMatch.getPlayer2(), ongoingMatch.getMatchState(), ongoingMatch.getWinner());

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            FinishedMatchEntity finishedMatchEntity = H2FinishedMatchMapper.toEntity(ongoingMatch);

            session.persist(finishedMatchEntity);
            tx.commit();

            return H2FinishedMatchMapper.toFinishedMatch(finishedMatchEntity);
        } catch (Exception e) {
            rollbackSafely(tx, e);

            if (isDuplicate(e)) {
                throw new AlreadyExistsException("Finished match already exists.", e);
            }

            throw new DatabaseException("Failed to save finished match", e);
        }
    }

    @Override
    public List<FinishedMatch> findAll() {
        log.debug("Finding all finished matches");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<FinishedMatchEntity> finishedMatchEntities = session.createQuery(FIND_ALL_MATCHES, FinishedMatchEntity.class)
                    .getResultList();
            return H2FinishedMatchMapper.toFinishedMatch(finishedMatchEntities);
        } catch (Exception e) {
            throw new DatabaseException("Failed to find finished matches", e);
        }
    }

    @Override
    public Integer countAll() {
        return null;
    }
}
