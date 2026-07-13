package util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class StringUtils {
    public String normalizeInput(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input must not be null or blank");
        }
        return input.trim().toLowerCase(Locale.ROOT);
    }
}
