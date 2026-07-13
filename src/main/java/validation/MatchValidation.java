package validation;

import exception.ValidationException;
import lombok.experimental.UtilityClass;
import util.StringUtils;

import java.util.UUID;

@UtilityClass
public class MatchValidation {
    // Класс должен заниматься только приходящими от пользователя данными.
        // А доменная модель должна сама контролировать своё состояние и отвечать за его корректность.
    private final String PLAYER_ONE = "player1";
    private final String PLAYER_TWO = "player2";
    private final String WINNER_MUST_BE_PLAYER_MESSAGE =
            "winner must be either " + PLAYER_ONE + " or " + PLAYER_TWO;

    public void validateMatchUuid(String uuid) {
        if (uuid == null) {
            throw new ValidationException("uuid cannot be null");
        }

        if (uuid.trim().isEmpty()) {
            throw new ValidationException("uuid cannot be empty or blank");
        }
    }

    public void validateUuidFormat(String uuid) {
        validateMatchUuid(uuid);
        try {
            UUID.fromString(uuid.trim());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("uuid has an invalid format");
        }
    }

    public void validateWinner(String winner) {
        if (winner == null) {
            throw new ValidationException("winner cannot be null");
        }

        if (winner.trim().isEmpty()) {
            throw new ValidationException("winner cannot be empty or blank");
        }

        String normalizedWinner = StringUtils.normalizeInput(winner);

        if (!normalizedWinner.equals(PLAYER_ONE) && !normalizedWinner.equals(PLAYER_TWO)) {
            throw new ValidationException(WINNER_MUST_BE_PLAYER_MESSAGE);
        }
    }
}
