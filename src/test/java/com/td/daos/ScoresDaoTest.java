package com.td.daos;

import com.td.domain.Score;
import com.td.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class ScoresDaoTest {
    @Autowired
    private ScoresDao scoresDao;

    @Autowired
    private UserDao userDao;

    private Long testUID;

    @Before
    public void regTestUser() {
        User user = userDao.createUser("testname", "testemail@mail.ru", "password", "Adventurer");
        this.testUID = user.getId();
    }

    @Test
    public void testScoresAdd() {
        User user = userDao.getUserById(testUID);
        assertNotNull(user);
        scoresDao.addScores(user, 100);
        List<Score> scores = user.getScores();
        assertNotNull(scores);
        assertEquals(1, scores.size());
        assertEquals(100, scores.get(0).getScore());
        User same = userDao.getUserById(testUID);
        List<Score> sameScores = same.getScores();
        assertNotNull(sameScores);
        assertEquals(1, sameScores.size());
        assertEquals(100, sameScores.get(0).getScore());
    }

    @Test
    public void testScoresListGet() {
        List<String> names = new ArrayList<>();
        int lowestScore = 100;
        int highestScore = 120;
        for (int i = lowestScore; i <= highestScore; ++i) {
            names.add(String.valueOf(i));
        }
        List<Score> scores = new ArrayList<>();
        names.forEach(name -> {
            User user = userDao.createUser(name, name + "@email.ru", "password", "Adventurer");
            scores.add(scoresDao.addScores(user, Integer.valueOf(name)));
            scores.add(scoresDao.addScores(user, Integer.valueOf(name)));
            scores.add(scoresDao.addScores(user, Integer.valueOf(name)));

        });

        List<Score> firstPage = scoresDao.getScoresList(5);
        assertNotNull(firstPage);
        assertEquals(firstPage.size(), 5);

        assertEquals(firstPage.get(0).getScore(), highestScore);
        assertEquals(firstPage.get(1).getScore(), highestScore);
        assertEquals(firstPage.get(2).getScore(), highestScore);

        assertEquals(firstPage.get(3).getScore(), highestScore - 1);
        assertEquals(firstPage.get(4).getScore(), highestScore - 1);

        List<Score> secondPage = scoresDao.getScoresList(firstPage.get(4).getId(), 5);

        assertEquals(secondPage.get(0).getScore(), highestScore - 1);

        assertEquals(secondPage.get(1).getScore(), highestScore - 2);
        assertEquals(secondPage.get(2).getScore(), highestScore - 2);
        assertEquals(secondPage.get(3).getScore(), highestScore - 2);

        assertEquals(secondPage.get(4).getScore(), highestScore - 3);

    }

}
