package util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PageUtil {
    public static int parsePage(String page) {
        if (page == null || page.isBlank()) {
            return 1;
        }

        return Math.max(Integer.parseInt(page.trim()), 1);
    }
}
