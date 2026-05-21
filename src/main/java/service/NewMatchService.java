package service;

import dao.OngoingMatchDao;
import dao.PlayerDao;
import exception.AlreadyExistsException;
import exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import model.OngoingMatch;
import model.Player;
import validation.PlayerNameValidation;

import java.util.UUID;

@Slf4j
public class NewMatchService {
    private final OngoingMatchDao ongoingMatchDao;
    private final PlayerDao playerDao;

    public NewMatchService(PlayerDao playerDao, OngoingMatchDao ongoingMatchDao) {
        this.playerDao = playerDao;
        this.ongoingMatchDao = ongoingMatchDao;
    }

    public UUID startNewMatch(String player1Name, String player2Name) {
        PlayerNameValidation.validatePlayerNames(player1Name, player2Name);
        Player player1 = findOrCreatePlayer(player1Name);
        Player player2 = findOrCreatePlayer(player2Name);
        UUID matchId = UUID.randomUUID();
        OngoingMatch ongoingMatch = new OngoingMatch(matchId, player1.getId(), player2.getId(), null);
        ongoingMatchDao.save(ongoingMatch);
        return matchId;
    }

    private Player findOrCreatePlayer(String playerName) {
        Player player;
        try {
            player = playerDao.findByName(playerName);
            log.info("Player with name={} already exists", playerName);
        } catch (NotFoundException e) {
            player = new Player(null, playerName);
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
