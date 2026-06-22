package dao;

import dto.FinishedMatchDto;
import model.OngoingMatch;

import java.util.List;

public interface MatchesDao {
    void save(OngoingMatch match);

    List<FinishedMatchDto> findAllMatches(int offset, int limit);

    List<FinishedMatchDto> findMatchesByPlayerName(String playerName, int offset, int limit);

    Integer countAllMatches();

    Integer countMatchesByPlayerName(String playerName);
}
