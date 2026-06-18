package model;

import exception.ValidationException;
import lombok.Getter;

@Getter
public class MatchState {
    private Integer player1Id;
    private Integer player2Id;
    private int player1Sets;
    private int player2Sets;
    private int player1GamesInSet;
    private int player2GamesInSet;
    private int player1PointsInGame;
    private int player2PointsInGame;
    private boolean tieBreak;
    private int player1TieBreakPoints;
    private int player2TieBreakPoints;
    private boolean finished;
    private Integer winnerPlayerId;

    public MatchState(Integer player1Id, Integer player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
    }

    public void awardPointTo(Integer pointWinnerPlayerId) {
        applyPoint(pointWinnerPlayerId);
        processGameTransition(pointWinnerPlayerId);
        processSetTransition();
        processMatchFinish();
    }

    private void applyPoint(Integer pointWinnerPlayerId) {
        if (tieBreak) {
            applyTieBreakPoint(pointWinnerPlayerId);
        } else {
            applyRegularGamePoint(pointWinnerPlayerId);
        }
    }

    private void applyTieBreakPoint(Integer pointWinnerPlayerId) {
        if (pointWinnerPlayerId.equals(player1Id)) {
            player1TieBreakPoints++;
        } else if (pointWinnerPlayerId.equals(player2Id)) {
            player2TieBreakPoints++;
        } else {
            throw new ValidationException("Point winner is not part of this match.");
        }
    }

    private void applyRegularGamePoint(Integer pointWinnerPlayerId) {
        if (pointWinnerPlayerId.equals(player1Id)) {
            player1PointsInGame++;
        } else if (pointWinnerPlayerId.equals(player2Id)) {
            player2PointsInGame++;
        } else {
            throw new ValidationException("Point winner is not part of this match.");
        }
    }

    private void processGameTransition(Integer pointWinnerPlayerId) {
        if (tieBreak) {
            if (isTieBreakFinished()) {
                awardGameToPointWinner(pointWinnerPlayerId);
            }
            return;
        }

        if (isRegularGameFinished()) {
            awardGameToPointWinner(pointWinnerPlayerId);
            resetRegularGamePoints();
        }
    }

    private boolean isRegularGameFinished() {
        return (player1PointsInGame >= 4 || player2PointsInGame >= 4)
                && Math.abs(player1PointsInGame - player2PointsInGame) >= 2;
    }

    private boolean isTieBreakFinished() {
        return (player1TieBreakPoints >= 7 || player2TieBreakPoints >= 7)
                && Math.abs(player1TieBreakPoints - player2TieBreakPoints) >= 2;
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

    private void resetRegularGamePoints() {
        player1PointsInGame = 0;
        player2PointsInGame = 0;
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
        player1PointsInGame = 0;
        player2PointsInGame = 0;
        tieBreak = false;
        player1TieBreakPoints = 0;
        player2TieBreakPoints = 0;
    }
}
