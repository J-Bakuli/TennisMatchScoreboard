package dao;

import dto.FinishedMatchDto;
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

    // Метод должен возвращать List<FinishedMatchEntity>
    List<FinishedMatchDto> findAllMatches(int offset, int limit);

    // Метод должен возвращать List<FinishedMatchEntity>
    List<FinishedMatchDto> findMatchesByPlayerName(String playerName, int offset, int limit);

    // Использование `long` для счётчиков, получаемых из БД, является более правильной практикой,
        // так как SQL-функция `COUNT` возвращает 64-битное число.
    // Можно назвать просто countAll
    Integer countAllMatches();

    // Использование `long` для счётчиков, получаемых из БД, является более правильной практикой,
        // так как SQL-функция `COUNT` возвращает 64-битное число.
    // Можно назвать countByPlayerName
    Integer countMatchesByPlayerName(String playerName);
}
