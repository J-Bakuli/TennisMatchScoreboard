package dao;

import model.FinishedMatch;
import model.OngoingMatch;

import java.util.List;

public interface MatchesDao {
    FinishedMatch save(OngoingMatch match);

    List<FinishedMatch> findAll();

    Integer countAllMatches();

    Integer countMatchesByPlayerName(String playerName);
}
