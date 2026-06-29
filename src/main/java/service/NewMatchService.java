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

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // Валидация имён игроков происходит внутри сервисного слоя, а не на "входе" в приложение.
        // Это не соответствует принципу быстрого отказа ("Fail Fast"):
        // Проверку корректности данных, пришедших от пользователя, следует проводить как можно раньше.
        // Валидация на уровне сервлета позволяет немедленно прервать обработку некорректного запроса и вернуть клиенту ошибку `400 Bad Request`.
        // Текущий подход заставляет приложение выполнять лишнюю работу, передавая невалидные данные дальше в сервисный слой.
        // Стоит запускать логику валидации из сервлета.

    // Класс способствует протечке данных из слоя персистентности (долговременного хранения данных) —
        // из БД — в доменные модели.

    private final PlayerDao playerDao;
    private final OngoingMatchDao ongoingMatchDao;

    // Можно использовать @RequiredArgsConstructor
    public NewMatchService(PlayerDao playerDao, OngoingMatchDao ongoingMatchDao) {
        this.playerDao = playerDao;
        this.ongoingMatchDao = ongoingMatchDao;
    }

    public UUID startNewMatch(String player1Name, String player2Name) {

        // Валидация должна быть как можно ближе ко входу данных в приложение
        PlayerValidation.validatePlayerNames(player1Name, player2Name);
        Player player1 = findOrCreatePlayer(player1Name);
        Player player2 = findOrCreatePlayer(player2Name);

        // ID матча лучше получать из DAO, а не генерировать в этом классе
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

            // Валидация обоих имён уже выполняется до запуска этого метода. Достаточно выполнить её один раз.
            PlayerValidation.validatePlayerName(playerName);
            player = playerDao.findByName(playerName);
            log.info("Player with name={} already exists", playerName);
        } catch (NotFoundException e) {
            player = new Player(null, playerName);
            PlayerValidation.validatePlayerForCreate(player);
            try {
                player = playerDao.save(player);

                // Здесь стоит ловить DataAccessException (которое будет выбрасывать DAO)
                    // и анализировать, является ли его причиной нарушение уникальности.
            } catch (AlreadyExistsException ae) {
                player = playerDao.findByName(playerName);
            }
            log.info("Created new Player with name={}", playerName);
        }
        return player;
    }
}
