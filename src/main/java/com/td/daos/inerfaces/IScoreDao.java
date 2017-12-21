package com.td.daos.inerfaces;

import com.td.domain.Score;
import com.td.domain.User;

import java.util.List;

public interface IScoreDao {

    Score addScores(User user, int scores);

    List<Score> getScoresList(Long mark, int limit);


    List<Score> getScoresList(int limit);


}
