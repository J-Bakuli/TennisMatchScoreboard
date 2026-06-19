package service;

import dao.OngoingMatchDao;
import dao.PlayerDao;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import model.MatchState;
import model.OngoingMatch;
import model.Player;
import validation.MatchValidation;
import validation.PlayerValidation;

import java.util.UUID;

@Slf4j
public class NewMatchService {
    private final PlayerDao playerDao;
    private final OngoingMatchDao ongoingMatchDao;

    public NewMatchService(PlayerDao playerDao, OngoingMatchDao ongoingMatchDao) {
        this.playerDao = playerDao;
        this.ongoingMatchDao = ongoingMatchDao;
    }

    public UUID startNewMatch(String player1Name, String player2Name) {
        PlayerValidation.validatePlayerNames(player1Name, player2Name);
        Player player1 = findOrCreatePlayer(player1Name);
        Player player2 = findOrCreatePlayer(player2Name);
        UUID matchId = UUID.randomUUID();
        MatchState matchState = new MatchState(player1.id(), player2.id());
        OngoingMatch ongoingMatch = new OngoingMatch(matchId, player1.id(), player2.id(), matchState);
        MatchValidation.validateOngoingMatch(ongoingMatch);
        ongoingMatchDao.save(ongoingMatch);
        return matchId;
    }

    private Player findOrCreatePlayer(String playerName) {
        Player player;
        try {
            PlayerValidation.validatePlayerName(playerName);
            player = playerDao.findByName(playerName);
            log.info("Player with name={} already exists", playerName);
        } catch (NotFoundException e) {
            player = new Player(null, playerName);
            PlayerValidation.validatePlayerForCreate(player);
            try {
                player = playerDao.save(player);
            } catch (AlreadyExistsException ae) {
                player = playerDao.findByName(playerName);
            }
            log.info("Created new Player with name={}", playerName);
        }
        return player;
    }
}
