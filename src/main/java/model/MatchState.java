package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
