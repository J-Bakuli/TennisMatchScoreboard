package mapper;

import dto.FinishedMatchDto;
import model.FinishedMatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class FinishedMatchDtoMapper {
    private static final DateTimeFormatter FINISHED_AT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private FinishedMatchDtoMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static FinishedMatchDto toDto(FinishedMatch match, String player1Name, String player2Name, String winnerName) {
        LocalDateTime finishedAt = match.getFinishedAt();
        String finishedAtFormatted = finishedAt.format(FINISHED_AT_FORMATTER);
        return new FinishedMatchDto(
                player1Name,
                player2Name,
                winnerName,
                finishedAt,
                finishedAtFormatted
        );
    }
}
