package validation;

import exception.ValidationException;

public class MatchesQueryValidation {

    // Класс называется MatchesQueryValidation, хотя не имеет никакого отношения к модели матча или запросам.
        // Больше подошло бы название PageUtil.

    // Если класс задуман как утилитный, то стоит сделать его final, а его конструктор private.
        // Можно использовать аннотацию @UtilityClass из Lombok

    // Класс валидирует и парсит значение — это нарушает Принцип единой ответственности (SRP).
        // Валидатор должен заниматься только валидацией.

    public static int parsePage(String page) {
        if (page == null || page.isBlank()) {
            return 1;
        }

        try {
            return Math.max(Integer.parseInt(page.trim()), 1);
        } catch (NumberFormatException e) {
            throw new ValidationException("Page must be a positive integer");
        }
    }
}
