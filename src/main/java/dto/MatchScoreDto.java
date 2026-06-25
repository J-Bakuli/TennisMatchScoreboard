package dto;

public record MatchScoreDto(

        // TODO: Нет полей для счёта в тай-брейке

        // Сейчас все поля, относящиеся к счёту игрока, дублируются для первого и второго игрока.
            // Такой подход делает классы большими и громоздкими и нарушает принцип DRY (Don't Repeat Yourself).
            // Также, чтобы добавить счёт в тай-брейке для каждого игрока, понадобится добавить два поля.
            // Можно ввести DTO для счёта одного игрока и хранить два таких DTO внутри MatchScoreDto.

    String uuid,
    String player1Name,
    String player2Name,
    int player1Sets,
    int player2Sets,
    int player1GamesInSet,
    int player2GamesInSet,
    String player1PointsDisplay, // Суффикс *Display можно убрать
    String player2PointsDisplay // Суффикс *Display можно убрать
) {
}
