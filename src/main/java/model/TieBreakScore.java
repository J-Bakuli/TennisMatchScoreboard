package model;

import lombok.Getter;

@Getter
public class TieBreakScore {

    // Все повторяющиеся или важные "магические" числа лучше вынести в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    private int player1TieBreakPoints;
    private int player2TieBreakPoints;

    // Перед начислением очка нет проверки, что тай-брейк не завершён.
        // Это позволяет увеличивать счёт до пределов значения int.
    // Вместо передачи в метод флага для обозначения того, какой игрок выиграл очко,
        // лучше передавать специальный enum (например TennisSide { FIRST, SECOND; }).
        // Это будет больше соответствовать ООП подходу и лучше читаться.
    public void awardPointTo(boolean isPlayer1) {
        if (isPlayer1) {
            player1TieBreakPoints++;
        } else {
            player2TieBreakPoints++;
        }
    }

    public boolean isFinished() {
        return (player1TieBreakPoints >= 7 || player2TieBreakPoints >= 7)
                && Math.abs(player1TieBreakPoints - player2TieBreakPoints) >= 2;
    }

    // Вместо того чтобы сбрасывать очки при старте нового тай-брейка,
        // лучше создавать новый объект гейма и добавлять его в коллекцию геймов, хранимую в объекте сета.
        // Это больше соответствует реальному теннисному матчу.
    public void reset() {
        player1TieBreakPoints = 0;
        player2TieBreakPoints = 0;
    }
}
