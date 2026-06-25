package dao;

import model.Player;

public interface PlayerDao {

    // TODO: Возвращение доменной модели Player из DAO нарушает Принцип разделения ответственности (Separation of Concerns)
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
        // Слой DAO не должен ничего знать о доменных моделях и работать с ними.
        // Преобразование "доменные модели <—> JPA Entity" — это задача сервисного слоя (через мапперы).

    // Лучше возвращать Optional<Player>, чтобы метод никогда не возвращал null
    Player findByName(String name);

    // Лучше возвращать Optional<Player>, чтобы метод никогда не возвращал null
    Player findById(Integer id);
    Player save(Player player);
}
