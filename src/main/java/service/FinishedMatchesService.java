package service;

import dao.MatchesDao;
import dao.PlayerDao;
import dto.FinishedMatchDto;
import exception.DatabaseException;
import exception.NotFoundException;
import model.FinishedMatch;
import model.OngoingMatch;
import validation.MatchValidation;

import java.util.Collections;
import java.util.List;

public class FinishedMatchesService {
    private final MatchesDao matchesDao;
    private final PlayerDao playerDao;

    public FinishedMatchesService(MatchesDao matchesDao, PlayerDao playerDao) {
        this.matchesDao = matchesDao;
        this.playerDao = playerDao;
    }

    public void saveFinishedMatch(OngoingMatch ongoingMatch) {
        MatchValidation.validateOngoingMatch(ongoingMatch);
        matchesDao.save(ongoingMatch);
    }

    public List<FinishedMatchDto> getAllFinishedMatches() {
        List<FinishedMatch> finishedMatches = matchesDao.findAll();

        if (finishedMatches.isEmpty()) {
            return Collections.emptyList();
        }

        return finishedMatches.stream()
                .map(match -> {
                    String player1Name = resolvePlayerNameById(match.getPlayer1Id(), "player1");
                    String player2Name = resolvePlayerNameById(match.getPlayer2Id(), "player2");
                    String winnerName = resolvePlayerNameById(match.getWinnerId(), "winner");

                    return new FinishedMatchDto(
                            player1Name,
                            player2Name,
                            winnerName,
                            match.getFinishedAt()
                    );
                })
                .toList(); //Todo оптимизировать позже

    }

    private String resolvePlayerNameById(Integer playerId, String role) {
        try {
            return playerDao.findById(playerId).getName();
        } catch (NotFoundException e) {
            throw new DatabaseException(
                    String.format("Failed to resolve %s name for finished match, playerId=%s", role, playerId), e);
        }
    }
}
