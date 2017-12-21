package com.td.daos;

import com.td.daos.exceptions.ScoreDaoInvalidData;
import com.td.daos.inerfaces.IScoreDao;
import com.td.domain.Score;
import com.td.domain.User;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScoresDao implements IScoreDao {

    private final EntityManager em;

    public ScoresDao(@Autowired EntityManager em) {
        this.em = em;
    }

    @Override
    public Score addScores(User user, int scores) {
        Score score = new Score(scores);
        user.addScores(score);
        try {
            em.persist(score);
            return score;
        } catch (PersistenceException except) {
            if (except.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violation = (ConstraintViolationException) except.getCause();
                throw new ScoreDaoInvalidData(violation.getConstraintName(), except);
            }
            throw except;
        }
    }

    @Override
    public List<Score> getScoresList(Long infimum, int limit) {
        Score bound = em.find(Score.class, infimum);
        if (bound == null) {
            return new ArrayList<>();
        }
        return em
                .createQuery(
                        "SELECT s "
                                .concat("FROM Score s WHERE s.score < :threshold ")
                                .concat("OR (s.score = :threshold AND s.id > :mark) ")
                                .concat("ORDER BY s.score DESC, s.id ASC"),
                        Score.class)
                .setParameter("threshold", bound.getScore())
                .setParameter("mark", infimum)
                .setMaxResults(limit)
                .getResultList();

    }

    @Override
    public List<Score> getScoresList(int limit) {
        return em.createQuery(
                "SELECT s "
                        .concat("FROM Score s ORDER BY s.score DESC, s.id ASC"),
                Score.class)
                .setMaxResults(limit)
                .getResultList();

    }


}
