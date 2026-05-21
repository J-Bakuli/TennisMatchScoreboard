package util;

import java.util.Locale;

public final class PlayerNameUtils {
    private PlayerNameUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
