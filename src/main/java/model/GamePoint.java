package model;

public enum GamePoint {
    LOVE,
    FIFTEEN,
    THIRTY,
    FORTY,
    ADVANTAGE;

    // Название nextInGame говорит о том, что класс знает о правилах гейма.
        // Хотя GamePoint является частью именно гейма, стоит назвать метод более общим названием next —
        // в соответствии с его назначением — вернуть следующее значение enum (а не следующее значение для логики гейма).
    public GamePoint nextInGame() {
        return switch (this) {
            case LOVE -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            // И в логике гейма и порядке значений в этом enum после FORTY следует ADVANTAGE. Поэтому стоит для FORTY возвращать ADVANTAGE и бросать исключение только на ADVANTAGE.
            case FORTY, ADVANTAGE -> throw new IllegalStateException("Game point cannot increment from " + this);
        };
    }

    // Доменная модель не должна знать то, как она отображается во View (иметь метод для преобразования значений) — это нарушает Принцип единой ответственности (SRP).
        // В идеале эта логика должна быть в маппере.
        // Допустимо сделать поле String value и возвращать его значение из метода с названием в духе: asString() или getStringValue().
    public String display() {
        return switch (this) {
            case LOVE -> "0";
            case FIFTEEN -> "15";
            case THIRTY -> "30";
            case FORTY -> "40";
            case ADVANTAGE -> "AD";
        };
    }
}
