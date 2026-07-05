package util;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class MatchesQueryUtils {

    // Класс называется MatchesQueryUtils, хотя не имеет никакого отношения к модели матча или запросам —
        // он работает только со строками. Стоит дать более подходящее и общее имя.


    // Функционал классов MatchesQueryUtils и PlayerUtils почти идентичен. Достаточно оставить один из классов.

    public static String normalizeFilter(String filter) {
        if (filter == null || filter.isBlank()) {
            return null;
        }
        return filter.trim().toLowerCase(Locale.ROOT);
    }
}
