package service;

import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import model.MatchState;
import validation.MatchValidation;

@Slf4j
public class MatchScoreCalculationService {

    // Валидация данных от пользователя происходит внутри сервисного слоя, а не на "входе" в приложение.
        // Это не соответствует принципу быстрого отказа ("Fail Fast"):
        // Проверку корректности данных, пришедших от пользователя, следует проводить как можно раньше.
        // Валидация на уровне сервлета позволяет немедленно прервать обработку некорректного запроса и вернуть клиенту ошибку `400 Bad Request`.
        // Текущий подход заставляет приложение выполнять лишнюю работу, передавая невалидные данные дальше в сервисный слой.
        // Стоит запускать логику валидации из сервлета.

    // Этот сервис не выполняет никакой важной работы, кроме вызова state.awardPointTo(pointWinnerPlayerId).
        // Можно его удалить (распределив неспецифичную для него ответственность по подходящим классам).

    public MatchState calculate(MatchState state, Integer pointWinnerPlayerId) {
        validatePointInput(state, pointWinnerPlayerId);
        MatchValidation.validateMatchState(state);
        state.awardPointTo(pointWinnerPlayerId);
        MatchValidation.validateMatchState(state);
        return state;
    }

    private void validatePointInput(MatchState state, Integer pointWinnerPlayerId) {
        if (state == null) {
            throw new ValidationException("Match state must not be null.");
        }
        if (state.getPlayer1Id() == null || state.getPlayer2Id() == null) {
            throw new ValidationException("Player id must not be null.");
        }
        if (pointWinnerPlayerId == null) {
            throw new ValidationException("Point winner id must not be null.");
        }

        // Доменная модель матча должна сама бросать исключение в случае,
            // если ID игрока, выигравшего очко не совпадает ни с одним из ID участников.
        if (!pointWinnerPlayerId.equals(state.getPlayer1Id())
                && !pointWinnerPlayerId.equals(state.getPlayer2Id())) {
            throw new ValidationException("Point winner is not part of this match.");
        }

        // Доменная модель матча должна сама бросать исключение при попытке добавить очко в завершившемся матче.
        if (state.isFinished()) {
            throw new ValidationException("Match is already finished.");
        }
    }
}
