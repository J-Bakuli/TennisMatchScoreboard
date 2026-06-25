package dao;

import exception.NotFoundException;
import model.OngoingMatch;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOngoingMatchDao implements OngoingMatchDao {
    private final Map<UUID, OngoingMatch> ongoingMatches = new ConcurrentHashMap<>();

    // Лучше, чтобы метод сам создавал ID матча (по аналогии с репозиторием, работающим с БД) и возвращал его,
        // а не принимал готовый ID в качестве аргумента.
    @Override
    public OngoingMatch save(OngoingMatch match) {
        ongoingMatches.put(match.getUuid(), match);
        return match;
    }

    @Override
    public OngoingMatch findByUuid(UUID uuid) {
        OngoingMatch match = ongoingMatches.get(uuid);
        if (match == null) {
            throw new NotFoundException("Ongoing match not found for uuid=" + uuid);
        }
        return match;
    }

    @Override
    public void removeByUuid(UUID uuid) {
        OngoingMatch removed = ongoingMatches.remove(uuid);
        if (removed == null) {
            throw new NotFoundException("Ongoing match not found for uuid=" + uuid);
        }
    }
}
