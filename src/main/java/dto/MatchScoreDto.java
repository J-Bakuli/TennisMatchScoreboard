package dto;

public record MatchScoreDto(
        String uuid,
        PlayerScoreDto player1,
        PlayerScoreDto player2
) {
}
