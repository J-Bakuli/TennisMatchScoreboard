package service;

import dao.OngoingMatchDao;
import dao.PlayerDao;
import dto.MatchScoreDto;
import exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import model.MatchState;
import model.OngoingMatch;
import model.Player;
import util.PlayerUtils;
import validation.MatchValidation;
import validation.PlayerValidation;

import java.util.UUID;


@Slf4j
public class OngoingMatchService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // Валидация и парсинг данных от пользователя происходит внутри сервисного слоя, а не на "входе" в приложение.
        // Это не соответствует принципу быстрого отказа ("Fail Fast"):
        // Проверку корректности данных, пришедших от пользователя, следует проводить как можно раньше.
        // Валидация на уровне сервлета позволяет немедленно прервать обработку некорректного запроса и вернуть клиенту ошибку `400 Bad Request`.
        // Текущий подход заставляет приложение выполнять лишнюю работу, передавая невалидные данные дальше в сервисный слой.
        // Стоит запускать логику валидации из сервлета и там же парсить данные.

    private final MatchScoreCalculationService matchScoreCalculationService;
    private final FinishedMatchesService finishedMatchesService;
    private final OngoingMatchDao ongoingMatchDao;
    private final PlayerDao playerDao;

    // Можно использовать @RequiredArgsConstructor
    public OngoingMatchService(MatchScoreCalculationService matchScoreCalculationService,
                               FinishedMatchesService finishedMatchesService,
                               OngoingMatchDao ongoingMatchDao,
                               PlayerDao playerDao) {
        this.matchScoreCalculationService = matchScoreCalculationService;
        this.finishedMatchesService = finishedMatchesService;
        this.ongoingMatchDao = ongoingMatchDao;
        this.playerDao = playerDao;
    }

    // Метод должен принимать UUID матча и ID игрока, выигравшего очко, а не строки для парсинга.
    // Метод нарушает Принцип разделения команд и запросов (см. файл "cqs-principle.md" в этом же пакете)
    public boolean processMatchScore(String uuidToConvert, String pointWinner) {
        OngoingMatch ongoingMatch = findOngoingMatch(uuidToConvert);
        MatchState matchState = ongoingMatch.getMatchState();
        Integer pointWinnerId = findWinnerPlayerId(pointWinner, ongoingMatch);
        UUID uuid = ongoingMatch.getUuid();

        synchronized (ongoingMatch) {
            try {
                if (matchState.isFinished()) {

                    // Когда из блока if происходит return, то остальной код можно писать без блока else.
                    return true;
                } else {
                    matchScoreCalculationService.calculate(matchState, pointWinnerId);
                }

                if (matchState.isFinished()) {
                    finishedMatchesService.saveFinishedMatch(ongoingMatch);

                    // Перед передачей в finishOngoingMatch() ID матча преобразуется в строку,
                        // а в методе finishOngoingMatch() обратно парсится в UUID.
                        // Такое двойное преобразование не имеет смысла — стоит сразу передавать в метод объект UUID.
                        // В текущей реализации, если убрать лишние преобразования, то вызов метода finishOngoingMatch()
                        // можно заменить на ongoingMatchDao.removeByUuid(uuid);
                    finishOngoingMatch(uuid.toString());
                    return true;
                }
            } catch (NotFoundException e) {
                log.info("Match uuid={} is already finished and removed from memory", uuid);
                return true;
            }
        }
        return false;
    }

    // Логику преобразование "доменная модель <—> DTO" стоит вынести в маппер.
    // Метод должен принимать UUID матча, а не строку для парсинга.
    // TODO: Сейчас при обработке каждого выигранного очка выполняется 2 запроса к БД.
        // То есть минимум 96 лишних запроса на обработку очков (2 запроса при обработке каждого из 48 очков — минимум для победы одного из игроков).
        // Это создаёт чрезмерно избыточную нагрузку на БД и снижает производительность.
        // Стоит пересмотреть логику этой части приложения и избавиться от такого количества лишних запросов.
    public MatchScoreDto findMatchScore(String uuidToConvert) {
        OngoingMatch ongoingMatch = findOngoingMatch(uuidToConvert);
        return new MatchScoreDto(
                ongoingMatch.getUuid().toString(),
                findPlayerNameById(ongoingMatch.getPlayer1()), // TODO: здесь при каждом вызове метода findMatchScore происходит обращение к БД
                findPlayerNameById(ongoingMatch.getPlayer2()), // TODO: здесь при каждом вызове метода findMatchScore происходит обращение к БД
                ongoingMatch.getMatchState().getPlayer1Sets(),
                ongoingMatch.getMatchState().getPlayer2Sets(),
                ongoingMatch.getMatchState().getPlayer1GamesInSet(),
                ongoingMatch.getMatchState().getPlayer2GamesInSet(),
                ongoingMatch.getMatchState().getPlayer1PointsDisplay(),
                ongoingMatch.getMatchState().getPlayer2PointsDisplay()
        );
    }

    // Метод должен принимать UUID матча, а не строку для парсинга.
    // Этот метод можно упразднить, оставив вместо него вызов ongoingMatchDao.findByUuid(uuid)
    private OngoingMatch findOngoingMatch(String uuidToConvert) {
        MatchValidation.validateMatchUuid(uuidToConvert);
        UUID uuid = MatchValidation.parseUuid(uuidToConvert);
        return ongoingMatchDao.findByUuid(uuid);
    }

    private String findPlayerNameById(Integer id) {
        PlayerValidation.validatePlayerId(id);
        Player player = playerDao.findById(id);

        // Данные, пришедшие из БД, можно не валидировать.
        PlayerValidation.validatePlayerForRead(player);
        return player.name();
    }

    // Логика этого метода должна выполняться из сервлета
    private Integer findWinnerPlayerId(String winner, OngoingMatch ongoingMatch) {
        MatchValidation.validateWinner(winner);
        MatchValidation.validateOngoingMatch(ongoingMatch);

        Integer player1Id = ongoingMatch.getPlayer1();
        Integer player2Id = ongoingMatch.getPlayer2();

        String normalizedWinner = PlayerUtils.normalizeInput(winner);

        // "Магические" строки лучше выносить в `private static final` константы с понятными именами.
            // Именованная константа делает код более семантически понятным.
        if ("player1".equals(normalizedWinner)) {
            return player1Id;
        }
        return player2Id;
    }

    // Метод должен принимать UUID матча, а не строку для парсинга.
    // Этот метод можно упразднить, оставив вместо него вызов ongoingMatchDao.removeByUuid(uuid)
    private void finishOngoingMatch(String uuidToConvert) {
        MatchValidation.validateMatchUuid(uuidToConvert);
        UUID uuid = MatchValidation.parseUuid(uuidToConvert);
        ongoingMatchDao.removeByUuid(uuid);
    }
}
