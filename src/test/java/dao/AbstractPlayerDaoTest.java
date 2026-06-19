package dao;

import exception.AlreadyExistsException;
import exception.NotFoundException;
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
                AlreadyExistsException.class,
                () -> playerDao.save(new Player(null, "  Nadal  "))
        );
    }

    @Test
    public void findByNameTest() {
        Player created = playerDao.save(new Player(null, "Safin"));
        Player found1 = playerDao.findByName("safin");
        Player found2 = playerDao.findByName("  SAFIN  ");
        Assertions.assertEquals(created.id(), found1.id());
        Assertions.assertEquals(created.id(), found2.id());
        Assertions.assertEquals("safin", found1.name());

        Assertions.assertThrows(
                NotFoundException.class,
                () -> playerDao.findByName("Nadal")
        );
    }

    @Test
    public void findByIdTest() {
        Player created = playerDao.save(new Player(null, "Safin"));
        Player found = playerDao.findById(created.id());

        Assertions.assertEquals(created.id(), found.id());
        Assertions.assertEquals("safin", found.name());

        Assertions.assertThrows(
                NotFoundException.class,
                () -> playerDao.findById(76)
        );
    }
}
