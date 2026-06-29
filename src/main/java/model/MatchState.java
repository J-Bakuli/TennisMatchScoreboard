package model;

import exception.ValidationException;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class MatchState {

    // Больше подошло бы название MatchScore — по аналогии с RegularGameScore и TieBreakScore

    // TODO: Класс отвечает за обработку очков в сете и матче —
        // это слишком большая ответственность для одного класса и нарушает SRP (Single Responsibility Principle).
        // Лучшим решением в этом направлении было бы, чтобы за счёт в сете и матче отвечали разные классы.
        // Такой подход больше соответствовал принципу единственной ответственности для каждого класса.

    // Все повторяющиеся или важные "магические" числа лучше вынести в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // Классу, который отвечает за счёт в сете, лучше не хранить флаг boolean tieBreak,
        // а иметь ссылку на абстрактный гейм (или список геймов), например, GameScore.
        // GameScore при этом будет общим предком для класса гейма и тай-брейка.
        // И класс, отвечающий за счёт в сете, будет делегировать обработку очка текущему гейму,
        // вызывая метод awardPointTo у объекта типа GameScore и не заботясь о том, стоит за ним обычный гейм или тай-брейк.

    private final Integer player1Id;
    private final Integer player2Id;
    private int player1Sets;
    private int player2Sets;
    private int player1GamesInSet;
    private int player2GamesInSet;
    private boolean tieBreak;

    @Getter(AccessLevel.NONE)
    private final RegularGameScore regularGame = new RegularGameScore();

    // Класс хранит ссылку на объект тай-брейка, даже если в матче не будет сыграно ни одного тай-брейка.
    @Getter(AccessLevel.NONE)
    private final TieBreakScore tieBreakScore = new TieBreakScore();

    // ID игрока в этом объекте — это ID из БД. Внутренняя информация из БД не должна "протекать" в доменные модели.
        // Вместо этого можно использовать просто их имена, доменные модели или специальный enum.
    public MatchState(Integer player1Id, Integer player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
    }

    public GamePoint getPlayer1PointsInGame() {
        return regularGame.getPlayer1PointsInGame();
    }

    public GamePoint getPlayer2PointsInGame() {
        return regularGame.getPlayer2PointsInGame();
    }


    // Доменная модель не должна знать то, как она отображается во View (иметь метод для преобразования значений)
    // — это нарушает Принцип единой ответственности (SRP).
        // В идеале эта логика должна быть в маппере.
    public String getPlayer1PointsDisplay() {
        return regularGame.getPlayer1PointsInGame().display();
    }

    // Доменная модель не должна знать то, как она отображается во View (иметь метод для преобразования значений)
    // — это нарушает Принцип единой ответственности (SRP).
        // В идеале эта логика должна быть в маппере.
    public String getPlayer2PointsDisplay() {
        return regularGame.getPlayer2PointsInGame().display();
    }

    public int getPlayer1TieBreakPoints() {
        return tieBreakScore.getPlayer1TieBreakPoints();
    }

    public int getPlayer2TieBreakPoints() {
        return tieBreakScore.getPlayer2TieBreakPoints();
    }

    public boolean isFinished() {
        return player1Sets == 2 || player2Sets == 2;
    }

    // Лучше не возвращать null. Можно возвращать Optional<T>, где T — доменная модель игрока, его имя или специальный enum.
    public Integer getWinnerPlayerId() {
        if (player1Sets == 2) {
            return player1Id;
        }
        if (player2Sets == 2) {
            return player2Id;
        }
        return null;
    }

    public void awardPointTo(Integer pointWinnerPlayerId) { // Можно назвать аргумент просто winnerId (пока это ID)
        boolean isPlayer1PointWinner = isPlayer1Winner(pointWinnerPlayerId);
        if (tieBreak) {
            tieBreakScore.awardPointTo(isPlayer1PointWinner);
        } else {
            regularGame.awardPointTo(isPlayer1PointWinner);
        }
        processGameTransition(pointWinnerPlayerId);
        processSetTransition();
    }

    // Метод выполняет две задачи — проверяет, принадлежит ли ID одному из игроков и
        // принадлежит ли ID первому игроку. Лучше разделить эти ответственности на два метода.
    private boolean isPlayer1Winner(Integer pointWinnerPlayerId) {
        if (pointWinnerPlayerId.equals(player1Id)) {
            return true;
        }
        if (pointWinnerPlayerId.equals(player2Id)) {
            return false;
        }

        // Неправильный ID игрока в этом методе — это не ошибка валидации (ValidationException),
                // а ошибка неправильного аргумента (IllegalArgumentException)
        throw new ValidationException("Point winner is not part of this match.");
    }

    // Неиформативное название метода
    // После реализации общего предка для гейма и тай-брейка, вызов этого метода можно будет заменить на обращение к новому классу.
    private void processGameTransition(Integer pointWinnerPlayerId) {
        if (tieBreak) {
            if (tieBreakScore.isFinished()) {
                awardGameToPointWinner(pointWinnerPlayerId);
            }
            return;
        }

        if (regularGame.isFinished()) {
            awardGameToPointWinner(pointWinnerPlayerId);
            regularGame.reset();
        }
    }

    // Неиформативное название метода
    private void processSetTransition() {
        if (shouldStartTieBreak()) {
            tieBreak = true;
            // Если тай-брейк только начался, то проверка, завершён ли сет не имеет смысл — в этом месте должен быть return.
        }
        if (isSetFinished()) {
            awardSetToSetWinner();
            resetSetState();
        }
    }

    private boolean isSetFinished() {
        if ((player1GamesInSet >= 6 || player2GamesInSet >= 6)
                && Math.abs(player1GamesInSet - player2GamesInSet) >= 2) {
            return true;
        }

        // Если один из игроков набрал 7 очков, то количество очков у второго не имеет значения —
            // можно их не проверять.
        return (player1GamesInSet == 7 && player2GamesInSet == 6)
                || (player1GamesInSet == 6 && player2GamesInSet == 7);
    }

    private boolean shouldStartTieBreak() {
        return player1GamesInSet == 6 && player2GamesInSet == 6 && !tieBreak;
    }

    private void awardGameToPointWinner(Integer pointWinnerPlayerId) {
        if (pointWinnerPlayerId.equals(player1Id)) {
            player1GamesInSet++;
        } else if (pointWinnerPlayerId.equals(player2Id)) {
            player2GamesInSet++;
        } else {

            // Проверка на то, что передан ID одного из участников матча должна быть в самом первом методе цепочки —
                // достаточно выполнить её один раз на входе ID в публичный метод.
            // Неправильный ID игрока в этом методе — это не ошибка валидации (ValidationException),
                // а ошибка неправильного аргумента (IllegalArgumentException)
            throw new ValidationException("Point winner is not part of this match.");
        }
    }

    private void awardSetToSetWinner() {
        if (player1GamesInSet > player2GamesInSet) {
            player1Sets++;
        } else if (player2GamesInSet > player1GamesInSet) {
            player2Sets++;
        } else {

            // Попытка засчитать победу в сете при равном счёте в этом методе —
                // это не ошибка валидации (ValidationException),
                // а сигнал о том, что метод был запущен в неподходящее время —
                // объект в неправильном для вызова этого метода состоянии (IllegalStateException)
            throw new ValidationException("Cannot award set when games are equal.");
        }
    }

    // Вместо того чтобы сбрасывать очки при старте нового сета,
        // лучше создавать новый объект сета и добавлять его в коллекцию сетов, хранимую в объекте матча.
        // Это больше соответствует реальному теннисному матчу.
    private void resetSetState() {
        player1GamesInSet = 0;
        player2GamesInSet = 0;
        regularGame.reset();
        tieBreak = false;
        tieBreakScore.reset();
    }
}
