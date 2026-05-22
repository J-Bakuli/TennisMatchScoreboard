package service;

import dao.OngoingMatchDao;
import model.OngoingMatch;
import validation.MatchValidation;

import java.util.UUID;

public class OngoingMatchService {
    private final OngoingMatchDao ongoingMatchDao;

    public OngoingMatchService(OngoingMatchDao ongoingMatchDao) {
        this.ongoingMatchDao = ongoingMatchDao;
    }

    public OngoingMatch findOngoingMatch(String uuidToConvert) {
        MatchValidation.validateMatchUuid(uuidToConvert);
        UUID uuid = UUID.fromString(uuidToConvert);
        return ongoingMatchDao.findByUuid(uuid);
    }
}
