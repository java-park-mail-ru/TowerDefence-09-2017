package com.td.daos;

import com.td.domain.Score;
import com.td.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Transactional
public class ScoresDaoTest {
    @Autowired
    private ScoresDao scoresDao;

    @Autowired
    private UserDao userDao;

    private Long testUID;

    @Before
    public void regTestUser() {
        User user = userDao.createUser("testname", "testemail@mail.ru", "password");
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

}
