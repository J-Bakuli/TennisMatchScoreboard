package service;

import dao.MatchesDao;
import model.OngoingMatch;
import validation.MatchValidation;

public class FinishedMatchesService {
    private final MatchesDao matchesDao;

    public FinishedMatchesService(MatchesDao matchesDao) {
        this.matchesDao = matchesDao;
    }

    public void saveFinishedMatch(OngoingMatch ongoingMatch) {
        MatchValidation.validateOngoingMatch(ongoingMatch);
        matchesDao.save(ongoingMatch);
    }
}
