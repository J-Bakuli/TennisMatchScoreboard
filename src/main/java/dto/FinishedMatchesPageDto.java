package dto;

import java.util.List;

public record FinishedMatchesPageDto(

        // FinishedMatchesPageDto — это DTO для передачи данных.
            // Хранение в нём готовых URL (previousPageUrl и nextPageUrl) смешивает данные и представление (view-логику).

        List<FinishedMatchDto> matches,
        int currentPage,
        int totalPages,
        String filterByPlayerName,
        String previousPageUrl,
        String nextPageUrl
) {
}
