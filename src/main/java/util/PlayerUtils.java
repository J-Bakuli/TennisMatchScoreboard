package util;

import java.util.Locale;

public final class PlayerUtils {

    // Класс называется PlayerUtils, хотя не имеет никакого отношения к модели Player.
        // Стоит дать более подходящее и общее имя.

    // Можно использовать аннотацию @UtilityClass из Lombok

    // Функционал классов MatchesQueryUtils и PlayerUtils почти идентичен. Достаточно оставить один из классов.

    private PlayerUtils() {

        // Конструктор приватный, поэтому недоступен для вызова за пределами класса.
            // Нет необходимости бросать в нём исключение.
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String normalizeInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input must not be null");
        }
        return input.trim().toLowerCase(Locale.ROOT);
    }
}
