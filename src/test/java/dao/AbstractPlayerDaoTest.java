package dao;

import exception.EntityAlreadyExistsException;
import model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class AbstractPlayerDaoTest {
    protected PlayerDao playerDao;

    @Test
    public void createPlayerTest() {
        Player saved = playerDao.save(new Player(null, "  Sharapova  "));
        Assertions.assertNotNull(saved.id());
        Assertions.assertEquals("sharapova", saved.name());

        playerDao.save(new Player(null, "Nadal"));
        Assertions.assertThrows(
                EntityAlreadyExistsException.class,
                () -> playerDao.save(new Player(null, "  Nadal  "))
        );
    }

    @Test
    public void findByNameTest() {
        Player created = playerDao.save(new Player(null, "Safin"));
        Player found1 = playerDao.findByName("safin").orElseThrow();
        Player found2 = playerDao.findByName("  SAFIN  ").orElseThrow();
        Assertions.assertEquals(created.id(), found1.id());
        Assertions.assertEquals(created.id(), found2.id());
        Assertions.assertEquals("safin", found1.name());

        Assertions.assertTrue(playerDao.findByName("Nadal").isEmpty());
    }

    @Test
    public void findByIdTest() {
        Player created = playerDao.save(new Player(null, "Safin"));
        Player found = playerDao.findById(created.id()).orElseThrow();

        Assertions.assertEquals(created.id(), found.id());
        Assertions.assertEquals("safin", found.name());

        Assertions.assertTrue(playerDao.findById(76).isEmpty());
    }
}
