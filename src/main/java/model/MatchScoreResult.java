package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchScoreResult {
    private MatchState state;
    private boolean finished;
    private Integer winnerPlayerId;
}
