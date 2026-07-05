package dto;

public record MatchScoreDto(

        // Сейчас все поля, относящиеся к счёту игрока, дублируются для первого и второго игрока.
            // Такой подход делает классы большими и громоздкими и нарушает принцип DRY (Don't Repeat Yourself).
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
