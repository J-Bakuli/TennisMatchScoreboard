package model;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public final class OngoingMatch {

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а вся значимая логика находится в других классах.
        // Если бы у класса были специализированные поведенческие методы,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // (см. файл "reach-anemic-model.md" в этом же пакете)
        // Например, здесь можно разместить метод, запускающий цепочку обработки выигранного очка,
        // проверку, принадлежит ли переданный ID одному из игроков,
        // маппинг "ID игрока <—> сторона мата" (чтобы уйти от флагов в качестве аргументов) и тп.

    private final UUID uuid;
    private final Integer player1;
    private final Integer player2;
    private final MatchState matchState;

    public OngoingMatch(UUID uuid, Integer player1, Integer player2, MatchState matchState) {
        if (uuid == null) {
            throw new IllegalArgumentException("ongoingMatch uuid cannot be null");
        }
        if (player1 == null) {
            throw new IllegalArgumentException("player1Id cannot be null");
        }
        if (player2 == null) {
            throw new IllegalArgumentException("player2Id cannot be null");
        }
        if (matchState == null) {
            throw new IllegalArgumentException("matchState cannot be null");
        }
        if (player1.equals(player2)) {
            throw new IllegalArgumentException("players 1 and 2 must be different");
        }
        if (!Objects.equals(player1, matchState.getPlayer1Id())) {
            throw new IllegalArgumentException("player1 from ongoingMatch must be the same as the one from matchState");
        }
        if (!Objects.equals(player2, matchState.getPlayer2Id())) {
            throw new IllegalArgumentException("player2 from ongoingMatch must be the same as the one from matchState");
        }
        this.uuid = uuid;
        this.player1 = player1;
        this.player2 = player2;
        this.matchState = matchState;
    }

    public void assertFinished() {
        if (!matchState.isFinished()) {
            throw new IllegalStateException("Match is not finished.");
        }
        Integer winnerId = matchState.getWinnerPlayerId();
        if (winnerId == null) {
            throw new IllegalStateException("Finished match must have a winner.");
        }
        if (!Objects.equals(winnerId, player1) && !Objects.equals(winnerId, player2)) {
            throw new IllegalStateException("Winner must be one of ongoing match players.");
        }
    }
}
