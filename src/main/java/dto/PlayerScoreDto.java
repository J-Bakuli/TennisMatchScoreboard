package dto;

public record PlayerScoreDto(
        String name,
        int sets,
        int gamesInSet,
        String points
) {
}
