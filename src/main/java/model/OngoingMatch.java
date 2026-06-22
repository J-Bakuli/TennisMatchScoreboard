package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class OngoingMatch {
    private final UUID uuid;
    private final Integer player1;
    private final Integer player2;
    private final MatchState matchState;
}
