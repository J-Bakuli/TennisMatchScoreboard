package util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class PlayerUtils {

    // Класс называется PlayerUtils, хотя не имеет никакого отношения к модели Player.
        // Стоит дать более подходящее и общее имя.
    // Функционал классов MatchesQueryUtils и PlayerUtils почти идентичен. Достаточно оставить один из классов.

    public static String normalizeInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input must not be null");
        }
        return input.trim().toLowerCase(Locale.ROOT);
    }
}
