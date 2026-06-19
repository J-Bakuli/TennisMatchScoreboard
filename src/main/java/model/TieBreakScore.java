package model;

import lombok.Getter;

@Getter
public class TieBreakScore {
    private int player1TieBreakPoints;
    private int player2TieBreakPoints;

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

    public void reset() {
        player1TieBreakPoints = 0;
        player2TieBreakPoints = 0;
    }
}
