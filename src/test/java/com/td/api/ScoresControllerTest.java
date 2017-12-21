package com.td.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.td.daos.ScoresDao;
import com.td.daos.UserDao;
import com.td.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class ScoresControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScoresDao scoresDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<User> users = new ArrayList<>();

    @Before
    public void init() {
        for (int i = 100; i < 120; ++i) {
            users.add(userDao.createUser(String.valueOf(i),
                    String.valueOf(i) + "@test.ru",
                    "password",
                    "Adventurer"));
        }

    }

    @Test
    public void testGetScoresList() throws Exception {
        users.forEach(user -> scoresDao.addScores(user, Integer.valueOf(user.getNickname())));
        mockMvc.perform(get("/api/scores/"))
                .andExpect(jsonPath("scores").isArray());
    }


}
