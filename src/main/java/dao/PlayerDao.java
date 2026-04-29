package dao;

import model.Player;

public interface PlayerDao extends BaseDao<Player> {
    Player findByName(String name);
    Player findById(Integer id);
}
