package model;

import lombok.Getter;

@Getter
public class RegularGameScore {
    private int player1PointsInGame;
    private int player2PointsInGame;

    public void awardPointTo(boolean isPlayer1) {
        if (isPlayer1) {
            player1PointsInGame++;
        } else {
            player2PointsInGame++;
        }
    }

    public boolean isFinished() {
        return (player1PointsInGame >= 4 || player2PointsInGame >= 4)
                && Math.abs(player1PointsInGame - player2PointsInGame) >= 2;
    }

    public void reset() {
        player1PointsInGame = 0;
        player2PointsInGame = 0;
    }
}
