package mapper;

import lombok.experimental.UtilityClass;
import persistence.entity.FinishedMatchEntity;
import persistence.entity.PlayerEntity;

import java.time.LocalDateTime;

@UtilityClass
public class H2FinishedMatchMapper {
    public static FinishedMatchEntity toEntity(PlayerEntity player1, PlayerEntity player2, PlayerEntity winner) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new FinishedMatchEntity(player1, player2, winner, localDateTime);
    }
}
