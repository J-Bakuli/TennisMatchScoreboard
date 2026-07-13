package mapper;

import lombok.experimental.UtilityClass;
import model.Player;
import entity.PlayerEntity;

@UtilityClass
public class H2PlayerMapper {
    public static Player toPlayer(PlayerEntity entity) {
        return new Player(entity.getId(), entity.getName());
    }

    public static PlayerEntity toEntity(String normalizedName) {
        return new PlayerEntity(normalizedName);
    }
}
