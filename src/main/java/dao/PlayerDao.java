package dao;

import model.Player;

public interface PlayerDao {
    Player findByName(String name);
    Player findById(Integer id);
    Player save(Player player);
}
