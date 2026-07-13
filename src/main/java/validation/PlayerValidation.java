package validation;

import exception.ValidationException;
import lombok.experimental.UtilityClass;
import model.Player;
import util.StringUtils;

@UtilityClass
public class PlayerValidation {
    private final int MIN_PLAYER_NAME_LENGTH = 2;
    private final int MAX_PLAYER_NAME_LENGTH = 20;
    private final String PLAYER_NAME_PATTERN = "^[a-z][a-z]*(?:[ '.\\-'][a-z][a-z]*)*$";
    private final String PLAYER_CANNOT_BE_NULL_MESSAGE = "Player cannot be null";
    private final String PLAYER_NAMES_MUST_BE_UNIQUE_MESSAGE =
            "Player names should be unique and cannot be equal";
    private final String PLAYER_NAME_INVALID_FORMAT_MESSAGE =
            "playerName must start with a letter and contain only lowercase letters, spaces, hyphens, apostrophes, and periods";

    // Проверка игрока поле создания объекта Player сводится к проверке его имени —
        // в клиентском коде можно сразу вызывать метод validatePlayerName().
    public void validatePlayerForCreate(Player player) {
        if (player == null) {
            throw new ValidationException(PLAYER_CANNOT_BE_NULL_MESSAGE);
        }

        validatePlayerName(player.name());
    }

    public void validatePlayerNames(String name1, String name2) {
        validatePlayerName(name1);
        validatePlayerName(name2);

        if (StringUtils.normalizeInput(name1).equals(StringUtils.normalizeInput(name2))) {
            throw new ValidationException(PLAYER_NAMES_MUST_BE_UNIQUE_MESSAGE);
        }
    }

    public void validatePlayerId(Integer id) {
        if (id == null) {
            throw new ValidationException("player id cannot be null");
        }

        if (id < 0) {
            throw new ValidationException("player id must be non-negative");
        }
    }

    public void validatePlayerName(String name) {
        if (name == null) {
            throw new ValidationException("playerName cannot be null");
        }

        if (name.trim().isEmpty()) {
            throw new ValidationException("playerName cannot be empty or blank");
        }

        String normalizedName = StringUtils.normalizeInput(name);
        if (normalizedName.length() < MIN_PLAYER_NAME_LENGTH || normalizedName.length() > MAX_PLAYER_NAME_LENGTH) {
            throw new ValidationException(
                    "playerName length must be between " + MIN_PLAYER_NAME_LENGTH + " and " + MAX_PLAYER_NAME_LENGTH
                            + " characters");
        }

        if (!normalizedName.matches(PLAYER_NAME_PATTERN)) {
            throw new ValidationException(PLAYER_NAME_INVALID_FORMAT_MESSAGE);
        }
    }
}
