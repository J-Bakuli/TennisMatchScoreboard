package mapper;

import persistence.entity.FinishedMatchEntity;
import persistence.entity.PlayerEntity;

import java.time.LocalDateTime;

public final class H2FinishedMatchMapper {

    // Можно использовать аннотацию @UtilityClass из Lombok

    private H2FinishedMatchMapper() {

        // Конструктор приватный, поэтому недоступен для вызова за пределами класса.
            // Нет необходимости бросать в нём исключение.
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static FinishedMatchEntity toEntity(PlayerEntity player1, PlayerEntity player2, PlayerEntity winner) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new FinishedMatchEntity(player1, player2, winner, localDateTime);
    }
}
