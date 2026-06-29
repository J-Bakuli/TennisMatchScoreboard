package util;

import java.util.Locale;

public final class MatchesQueryUtils {

    // Класс называется MatchesQueryUtils, хотя не имеет никакого отношения к модели матча или запросам —
        // он работает только со строками. Стоит дать более подходящее и общее имя.

    // Можно использовать аннотацию @UtilityClass из Lombok

    // Функционал классов MatchesQueryUtils и PlayerUtils почти идентичен. Достаточно оставить один из классов.

    private MatchesQueryUtils() {

        // Конструктор приватный, поэтому недоступен для вызова за пределами класса.
            // Нет необходимости бросать в нём исключение.
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String normalizeFilter(String filter) {
        if (filter == null || filter.isBlank()) {
            return null;
        }
        return filter.trim().toLowerCase(Locale.ROOT);
    }
}
