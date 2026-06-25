package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public final class OngoingMatch {

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а вся значимая логика находится в других классах.
        // Если бы у класса были специализированные поведенческие методы,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // (см. файл "reach-anemic-model.md" в этом же пакете)
        // Например, здесь можно разместить метод, запускающий цепочку обработки выигранного очка,
        // проверку, принадлежит ли переданный ID одному из игроков,
        // маппинг "ID игрока <—> сторона мата" (чтобы уйти от флагов в качестве аргументов) и тп.

    private final UUID uuid;
    private final Integer player1;
    private final Integer player2;
    private final MatchState matchState;
}
