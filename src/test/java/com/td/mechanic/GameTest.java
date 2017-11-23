package com.td.mechanic;

import com.td.daos.UserDao;
import com.td.domain.User;
import com.td.game.GameSession;
import com.td.game.domain.Wave;
import com.td.game.gameobjects.Monster;
import com.td.game.gameobjects.Path;
import com.td.game.services.GameSessionService;
import com.td.game.services.MonsterWaveGenerator;
import com.td.game.services.MonsterWaveProcessorService;
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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class GameTest {

    @Autowired
    private GameSessionService gameSessionService;

    @Autowired
    private MonsterWaveProcessorService waveProcessor;

    @Autowired
    private MonsterWaveGenerator waveGenerator;

    @Autowired
    private UserDao dao;

    @Before
    public void initialize() {
        User us1 = dao.createUser("us1", "us1@mail.ru", "uuuuu");
        User us2 = dao.createUser("us2", "us2@mail.ru", "uuuuu");
        List<User> users = new ArrayList<>();
        users.add(us1);
        users.add(us2);
        gameSessionService.startGame(users);
    }

    @After
    public void deinitialize() {
        for (GameSession session : gameSessionService.getSessions()) {
            gameSessionService.finishGame(session);
        }
    }

    @Test
    public void testSessionInitialized() {
        Set<GameSession> sessions = gameSessionService.getSessions();
        assertTrue(sessions.size() == 1);
        for (GameSession session : sessions) {
            assertTrue(session.getPlayers().size() == 2);
            assertTrue(session.getPlayers().stream().allMatch(player ->
                    player.getMoney() == 100 && player.getScores() == 0
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
        Set<GameSession> sessions = gameSessionService.getSessions();
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

}
