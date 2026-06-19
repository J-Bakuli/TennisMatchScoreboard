package model;

import exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class MatchState {
    private Integer player1Id;
    private Integer player2Id;
    private int player1Sets;
    private int player2Sets;
    private int player1GamesInSet;
    private int player2GamesInSet;
    private boolean tieBreak;
    private boolean finished;
    private Integer winnerPlayerId;

    @Getter(AccessLevel.NONE)
    private final RegularGameScore regularGame = new RegularGameScore();

    @Getter(AccessLevel.NONE)
    private final TieBreakScore tieBreakScore = new TieBreakScore();

    public MatchState(Integer player1Id, Integer player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
    }

    public int getPlayer1PointsInGame() {
        return regularGame.getPlayer1PointsInGame();
    }

    public int getPlayer2PointsInGame() {
        return regularGame.getPlayer2PointsInGame();
    }

    public int getPlayer1TieBreakPoints() {
        return tieBreakScore.getPlayer1TieBreakPoints();
    }

    public int getPlayer2TieBreakPoints() {
        return tieBreakScore.getPlayer2TieBreakPoints();
    }

    public void awardPointTo(Integer pointWinnerPlayerId) {
        boolean isPlayer1 = isPlayer1Winner(pointWinnerPlayerId);
        if (tieBreak) {
            tieBreakScore.awardPointTo(isPlayer1);
        } else {
            regularGame.awardPointTo(isPlayer1);
        }
        processGameTransition(pointWinnerPlayerId);
        processSetTransition();
        processMatchFinish();
    }

    private boolean isPlayer1Winner(Integer pointWinnerPlayerId) {
        if (pointWinnerPlayerId.equals(player1Id)) {
            return true;
        }
        if (pointWinnerPlayerId.equals(player2Id)) {
            return false;
        }
        throw new ValidationException("Point winner is not part of this match.");
    }

    private void processGameTransition(Integer pointWinnerPlayerId) {
        if (tieBreak) {
            if (tieBreakScore.isFinished()) {
                awardGameToPointWinner(pointWinnerPlayerId);
            }
            return;
        }

        if (regularGame.isFinished()) {
            awardGameToPointWinner(pointWinnerPlayerId);
            regularGame.reset();
        }
    }

    private void processSetTransition() {
        if (shouldStartTieBreak()) {
            tieBreak = true;
        }
        if (isSetFinished()) {
            awardSetToSetWinner();
            resetSetState();
        }
    }

    private boolean isSetFinished() {
        if ((player1GamesInSet >= 6 || player2GamesInSet >= 6)
                && Math.abs(player1GamesInSet - player2GamesInSet) >= 2) {
            return true;
        }
        return (player1GamesInSet == 7 && player2GamesInSet == 6)
                || (player1GamesInSet == 6 && player2GamesInSet == 7);
    }

    private boolean shouldStartTieBreak() {
        return player1GamesInSet == 6 && player2GamesInSet == 6 && !tieBreak;
    }

    private void processMatchFinish() {
        if (player1Sets == 2) {
            finished = true;
            winnerPlayerId = player1Id;
        } else if (player2Sets == 2) {
            finished = true;
            winnerPlayerId = player2Id;
        }
    }

    private void awardGameToPointWinner(Integer pointWinnerPlayerId) {
        if (pointWinnerPlayerId.equals(player1Id)) {
            player1GamesInSet++;
        } else if (pointWinnerPlayerId.equals(player2Id)) {
            player2GamesInSet++;
        } else {
            throw new ValidationException("Point winner is not part of this match.");
        }
    }

    private void awardSetToSetWinner() {
        if (player1GamesInSet > player2GamesInSet) {
            player1Sets++;
        } else if (player2GamesInSet > player1GamesInSet) {
            player2Sets++;
        } else {
            throw new ValidationException("Cannot award set when games are equal.");
        }
    }

    private void resetSetState() {
        player1GamesInSet = 0;
        player2GamesInSet = 0;
        regularGame.reset();
        tieBreak = false;
        tieBreakScore.reset();
    }
}
