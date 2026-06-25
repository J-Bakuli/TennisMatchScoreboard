package dao;

import model.OngoingMatch;

import java.util.UUID;

public interface OngoingMatchDao {
    OngoingMatch save(OngoingMatch match);

    // Лучше возвращать Optional<OngoingMatch>, чтобы метод никогда не возвращал null
    OngoingMatch findByUuid(UUID uuid);

    void removeByUuid(UUID uuid);
}
