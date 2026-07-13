package util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UuidUtil {
    public UUID parseUuid(String uuid) {
        return UUID.fromString(uuid.trim());
    }
}
