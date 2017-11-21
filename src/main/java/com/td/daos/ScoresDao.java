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

@Service
@Transactional
public class ScoresDao implements IScoreDao {

    private final EntityManager em;

    public ScoresDao(@Autowired EntityManager em) {
        this.em = em;
    }

    @Override
    public void addScores(User user, int scores) {
        Score score = new Score(scores);
        user.addScores(score);
        try {
            em.persist(score);
        } catch (PersistenceException except) {
            if (except.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violation = (ConstraintViolationException) except.getCause();
                throw new ScoreDaoInvalidData(violation.getConstraintName(), except);
            }
            throw except;
        }
    }
}
