package validation;

import exception.ValidationException;

public class MatchValidation {
    public static void validateMatchUuid(String uuid) {
        if (uuid == null) {
            throw new ValidationException("uuid cannot be null");
        }

        if (uuid.trim().isEmpty()) {
            throw new ValidationException("uuid cannot be empty or blank");
        }
    }
}
