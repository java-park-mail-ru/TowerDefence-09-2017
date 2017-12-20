package com.td.mechanic;

import com.td.daos.UserDao;
import com.td.domain.User;
import com.td.game.GameContext;
import com.td.game.GameSession;
import com.td.game.domain.*;
import com.td.game.gameobjects.Monster;
import com.td.game.gameobjects.Path;
import com.td.game.gameobjects.Tower;
import com.td.game.resource.ResourceFactory;
import com.td.game.services.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.StampedLock;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class GameTest {
    @Autowired
    public ResourceFactory resources;

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private MonsterWaveProcessorService waveProcessor;

    @Autowired
    private TowerManager towerManager;

    @Autowired
    private TowerShootingService shootingService;

    @Autowired
    private UserDao dao;

    private GameContext context;

    @Before
    public void initialize() {
        context = new GameContext(new StampedLock());
        User us1 = dao.createUser("us1", "us1@mail.ru", "uuuuu", "Adventurer");
        User us2 = dao.createUser("us2", "us2@mail.ru", "uuuuu", "Adventurer");
        List<User> users = new ArrayList<>();
        users.add(us1);
        users.add(us2);
        gameSessionService.startGame(users, context);
    }

    @After
    public void deinitialize() {
        for (GameSession session : context.getSessions()) {
            gameSessionService.finishGame(session, context);
        }
    }

    @Test
    public void testSessionInitialized() {
        Set<GameSession> sessions = context.getSessions();
        GameParams params = resources.loadResource("gameParams/GameParams_2.json", GameParams.class);

        assertTrue(sessions.size() == 1);
        for (GameSession session : sessions) {
            assertTrue(session.getPlayers().size() == 2);
            assertTrue(session.getPlayers().stream().allMatch(player ->
                    params.getInitialMoney() == player.getMoney() && player.getScores() == 0
            ));
            assertTrue(session.getWaveNumber() == 0);
            Wave wave = session.getCurrentWave();
            assertTrue(wave.getStatus() == Wave.WaveStatus.PENDING);
            Monster monster = wave.getPending().peek();
            assertNotNull(monster);
            assertTrue(wave.getPassed().size() == 0);
            assertTrue(wave.getRunning().size() == 0);
            assertTrue(wave.getLocalTimeBuffer() == 10000);
            assertTrue(session.getTowers().size() == 0);
        }
    }


    @Test
    public void testWaveProcessing() {
        Set<GameSession> sessions = context.getSessions();
        assertEquals(1, sessions.size());
        for (GameSession session : sessions) {
            Wave wave = session.getCurrentWave();
            assertEquals(1, wave.getPathBindings().size());

            assertEquals(Wave.WaveStatus.PENDING, wave.getStatus());
            assertEquals(1, wave.getPending().size());

            waveProcessor.processWave(session, MonsterWaveGenerator.WAVE_START_DELAY);
            assertEquals(Wave.WaveStatus.STARTED, wave.getStatus());
            assertEquals(1, wave.getPending().size());

            waveProcessor.processWave(session, MonsterWaveGenerator.NEW_MONSTER_DELAY);
            assertEquals(1, wave.getRunning().size());
            assertEquals(Wave.WaveStatus.RUNNING, wave.getStatus());

            Map<Path, List<Monster>> bindings = wave.getPathBindings();
            for (Map.Entry<Path, List<Monster>> entry : bindings.entrySet()) {
                assertEquals(1, entry.getValue().size());
                Monster monster = entry.getValue().get(0);
                monster.setCoord(entry.getKey().getLastPoint());
                Integer old = session.getHp();
                waveProcessor.processPassedMonsters(session);
                assertEquals(old - monster.getWeight(), session.getHp());
                assertEquals(1, wave.getPassed().size());
            }
            waveProcessor.processWave(session, 50);
            assertEquals(Wave.WaveStatus.FINISHED, wave.getStatus());
        }
    }

    @Test
    public void testTowersPlacement() {
        Set<GameSession> sessions = context.getSessions();
        assertEquals(1, sessions.size());

        for (GameSession session : sessions) {
            Player firstPlayer = session.getPlayers().get(0);
            Player secondPlayer = session.getPlayers().get(1);
            GameMap map = session.getGameMap();

            //must be placed
            TowerManager.TowerOrder firstPlayerOrder = new TowerManager.TowerOrder(0, 2, 101, firstPlayer.getId());

            towerManager.processOrder(session, firstPlayerOrder);
            List<Tower> towers = session.getTowers();
            assertEquals(1, towers.size());

            Tower tower = towers.get(0);

            assertSame(tower.getOwner(), firstPlayer);
            assertEquals(tower.getTilePosition(), new Point<>(0L, 2L));
            assertSame(map.getTile(tower.getTilePosition()).getOwner(), tower);

            //must NOT be placed
            TowerManager.TowerOrder secondPlayerOrder = new TowerManager.TowerOrder(0, 2, 101, secondPlayer.getId());
            towerManager.processOrder(session, secondPlayerOrder);

            assertSame(towers, session.getTowers());
            assertEquals(1, towers.size());
            assertSame(map.getTile(tower.getTilePosition()).getOwner(), tower);

        }
    }

    @Test
    public void testTowerShooting() {
        Set<GameSession> sessions = context.getSessions();
        assertEquals(1, sessions.size());

        for (GameSession session : sessions) {
            Player firstPlayer = session.getPlayers().get(0);
            TowerManager.TowerOrder towerOrder = new TowerManager.TowerOrder(0, 2, 101, firstPlayer.getId());
            towerManager.processOrder(session, towerOrder);
            Monster monster = session.getCurrentWave().getPending().peek();
            waveProcessor.processWave(session, MonsterWaveGenerator.WAVE_START_DELAY);
            waveProcessor.processWave(session, MonsterWaveGenerator.NEW_MONSTER_DELAY);

            Tower tower = session.getTowers().get(0);
            Area shootingArea = tower.getRangeArea();

            assertTrue(shootingArea.checkCollision(monster));
            assertTrue(tower.isReady());
            Integer maxHp = monster.getHp();
            shootingService.processTowerShooting(session);
            List<ShotEvent> events = session.getShotEvents();
            assertEquals(1, events.size());
            assertEquals(maxHp - tower.getDamage(), monster.getHp());
            assertEquals(firstPlayer.getScores(), monster.getReward() * tower.getDamage());
            assertFalse(tower.isReady());
        }
    }

}
