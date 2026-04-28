package dao;

import model.OngoingMatch;

import java.util.UUID;

public interface OngoingMatchDao {
    OngoingMatch save(OngoingMatch match);

    OngoingMatch findByUuid(UUID uuid);

    void removeByUuid(UUID uuid);
}
