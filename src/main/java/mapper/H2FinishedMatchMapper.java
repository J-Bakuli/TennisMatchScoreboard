package mapper;

import exception.ValidationException;
import model.OngoingMatch;
import persistence.entity.FinishedMatchEntity;
import persistence.entity.PlayerEntity;

public final class H2FinishedMatchMapper {
    private H2FinishedMatchMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static FinishedMatchEntity toEntity(OngoingMatch ongoingMatch) {
        if (ongoingMatch == null) {
            throw new ValidationException("ongoingMatch cannot be null");
        }

        if (!ongoingMatch.getMatchState().isFinished()) {
            throw new ValidationException("matchState must be finished for finishedMatch mapping");
        }

        if (ongoingMatch.getWinner() == null) {
            throw new ValidationException("winner cannot be null for finishedMatch");
        }

        FinishedMatchEntity entity = new FinishedMatchEntity();
        PlayerEntity player1 = H2PlayerMapper.toEntityById(ongoingMatch.getPlayer1());
        PlayerEntity player2 = H2PlayerMapper.toEntityById(ongoingMatch.getPlayer2());
        PlayerEntity winner = H2PlayerMapper.toEntityById(ongoingMatch.getWinner());

        entity.setPlayer1(player1);
        entity.setPlayer2(player2);
        entity.setWinner(winner);
        return entity;
    }
}
