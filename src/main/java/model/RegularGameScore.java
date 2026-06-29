package model;

import lombok.Getter;

@Getter
public class RegularGameScore {

    // Значение GamePoint.LOVE можно вынести в константу с понятным названием, например INITIAL_GAME_POINT.

    // Вместо передачи в методы флага для обозначения того, какой игрок выиграл очко,
        // лучше передавать специальный enum (например TennisSide { FIRST, SECOND; }).
        // Это будет больше соответствовать ООП подходу и лучше читаться.

    private GamePoint player1PointsInGame = GamePoint.LOVE;
    private GamePoint player2PointsInGame = GamePoint.LOVE;
    private boolean isGameFinished;

    public void awardPointTo(boolean isPlayer1PointWinner) {

        // TODO: Сбрасывание флага isGameFinished позволяет начислять очки в уже завершённом гейме.
            // Например, при счёте 40:0, и выигрыше очка первым игроком счёт не изменится и
            // вызов метода regularGameScore.awardPointTo(false)
            // будет продолжать начислять очки второму игроку до счёта 40:AD.
            // И дальше начнётся режим больше-меньше.
            // Вместо этого при вызове метода awardPointTo на завершённом гейме должно бросаться исключение,
            // так как это не является ожидаемым и соответствующим бизнес-логике поведением.
        isGameFinished = false;
        GamePoint winner = getGamePointFor(isPlayer1PointWinner);
        GamePoint loser = getGamePointFor(!isPlayer1PointWinner);

        if (winner == GamePoint.ADVANTAGE) {
            isGameFinished = true;
            return;
        }

        if (winner == GamePoint.FORTY) {
            if (loser == GamePoint.FORTY) {
                setGamePoint(isPlayer1PointWinner, GamePoint.ADVANTAGE);
            } else if (loser == GamePoint.ADVANTAGE) {
                setDeuce();
            } else {
                isGameFinished = true;
            }
            return;
        }
        setGamePoint(isPlayer1PointWinner, winner.nextInGame());
    }

    // Публичный геттер isGameFinished() для поля isGameFinished уже создаётся аннотацией @Getter над классом
    public boolean isFinished() {
        return isGameFinished;
    }

    // Вместо того чтобы сбрасывать очки при старте нового гейма,
        // лучше создавать новый объект гейма и добавлять его в коллекцию геймов, хранимую в объекте сета.
        // Это больше соответствует реальному теннисному матчу.
    public void reset() {
        player1PointsInGame = GamePoint.LOVE;
        player2PointsInGame = GamePoint.LOVE;
        isGameFinished = false;
    }

    private GamePoint getGamePointFor(boolean isPlayer1) {
        return isPlayer1 ? player1PointsInGame : player2PointsInGame;
    }

    private void setGamePoint(boolean isPlayer1, GamePoint value) {
        if (isPlayer1) {
            player1PointsInGame = value;
        } else {
            player2PointsInGame = value;
        }
    }

    // Метод устанавливает очки до 40 у обоих игроков, хотя при переходе в состояние "ровно"
        // счёт должен сброситься только у игрока, который перед этим владел преимуществом,
        // или увеличиться у игрока, у которого до этого было 30.
        // Установка счёта для обоих игроков способствует маскировке ошибок.
        // Например, в случае ошибочного вызова этого метода в неподходящий момент гейма (40:15)
        // он "тихо" установит счёт обоих игроков в значение 40.
    private void setDeuce() {
        player1PointsInGame = GamePoint.FORTY;
        player2PointsInGame = GamePoint.FORTY;
    }
}
