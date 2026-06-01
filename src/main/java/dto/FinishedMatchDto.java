package dto;

import java.time.LocalDateTime;

public record FinishedMatchDto(
        String player1Name,
        String player2Name,
        String winnerName,
        LocalDateTime finishedAt,
        String finishedAtFormatted
) {
}
