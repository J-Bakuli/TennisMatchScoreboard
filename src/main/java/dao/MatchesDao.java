package dao;

import entity.FinishedMatchEntity;
import model.OngoingMatch;

import java.util.List;

public interface MatchesDao {

    // TODO: Использование доменной модели OngoingMatch или любых DTO в DAO
        // нарушает Принцип разделения ответственности (Separation of Concerns)
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
        // Слой DAO не должен ничего знать о доменных моделях и работать с ними.
        // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя.

    // Метод должен принимать FinishedMatchEntity
    void save(OngoingMatch match);

    List<FinishedMatchEntity> findAllMatches(int offset, int limit);

    List<FinishedMatchEntity> findMatchesByPlayerName(String playerName, int offset, int limit);

    long countAll();

    long countByPlayerName(String playerName);
}
