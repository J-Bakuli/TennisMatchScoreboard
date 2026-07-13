package validation;

import exception.ValidationException;

public class PageValidation {
    public static void validatePage(String page) {
        if (page == null || page.isBlank()) {
            return;
        }

        try {
            Integer.parseInt(page.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Page must be a positive integer");
        }
    }
}
