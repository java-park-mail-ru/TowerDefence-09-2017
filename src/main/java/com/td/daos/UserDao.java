package com.td.daos;

import com.td.daos.exceptions.UserDaoAlreadyExists;
import com.td.daos.exceptions.UserDaoInvalidData;
import com.td.daos.exceptions.UserDaoUpdateFail;
import com.td.daos.inerfaces.IUserDao;
import com.td.domain.GameProfile;
import com.td.domain.User;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserDao implements IUserDao {
    private final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private final EntityManager em;

    public UserDao(@Autowired EntityManager em) {
        this.em = em;
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public boolean checkUserByEmail(String email) {
        Long count = em.createQuery("SELECT count(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count == 1;
    }

    @Override
    public boolean checkUserByNickname(String nickname) {
        Long count = em.createQuery("SELECT count(u) FROM User u WHERE u.nickname = :nickname", Long.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
        return count == 1;
    }


    @Override
    public boolean checkUserById(Long id) {
        Long count = em.createQuery("SELECT count(u) FROM User u WHERE u.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count == 1;

    }

    @Override
    public User getUserByNickname(String nickname) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
            return user;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User getUserById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> getUsersByNicknames(List<String> nicknames) {
        return em.createQuery("SELECT u FROM User u WHERE u.nickname IN (:nicknames)", User.class)
                .setParameter("nicknames", nicknames)
                .getResultList();
    }

    @Override
    public User createUser(String nickname, String email, String password, String gameClass) {
        try {
            User user = new User();
            user.setNickname(nickname);
            user.setEmail(email);
            user.setPassword(password);
            user.setProfile(new GameProfile(gameClass));
            return storeUser(user);
        } catch (PersistenceException except) {
            if (except.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violation = (ConstraintViolationException) except.getCause();
                throw new UserDaoInvalidData(violation.getConstraintName(), except);
            }
            throw except;
        }
    }

    @Override
    public User storeUser(User user) {
        try {
            em.persist(user);
            return user;
        } catch (EntityExistsException except) {
            throw new UserDaoAlreadyExists(user.getId(), except);
        } catch (PersistenceException except) {
            if (except.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violation = (ConstraintViolationException) except.getCause();
                throw new UserDaoInvalidData(violation.getConstraintName(), except);
            }
            throw except;
        }
    }

    @Override
    public User updateUserById(Long id, String email, String login, String password) {
        try {
            User user = getUserById(id);
            user.updateEmail(email);
            user.updateNickname(login);
            user.updatePassword(password);
            em.flush();
            return user;
        } catch (PersistenceException except) {
            if (except.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violation = (ConstraintViolationException) except.getCause();
                throw new UserDaoInvalidData(violation.getConstraintName(), except);
            }
            throw except;
        }
    }

    @Override
    public User updateUserEmail(User user, String email) {
        try {
            user.setEmail(email);
            em.flush();
            return user;
        } catch (PersistenceException except) {
            if (except.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violation = (ConstraintViolationException) except.getCause();
                throw new UserDaoUpdateFail("email", violation.getConstraintName(), except);
            }
            throw except;
        }
    }

    @Override
    public User updateUserNickname(User user, String nickname) {
        try {
            user.updateNickname(nickname);
            em.flush();
            return user;
        } catch (PersistenceException except) {
            if (except.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violation = (ConstraintViolationException) except.getCause();
                throw new UserDaoUpdateFail("nickname", violation.getConstraintName(), except);
            }
            throw except;
        }
    }

    @Override
    public User updateUserPassword(User user, String password) {
        try {
            user.updatePassword(password);
            em.flush();
            return user;
        } catch (PersistenceException except) {
            if (except.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violation = (ConstraintViolationException) except.getCause();
                throw new UserDaoUpdateFail("password", violation.getConstraintName(), except);
            }
            throw except;
        }
    }


    @Override
    public void removeUser(User user) {
        em.remove(user);
    }

    @Override
    public void removeUserById(Long id) {
        removeUserByParams(id, null, null);

    }

    @Override
    public void removeUserByEmail(String email) {
        removeUserByParams(null, email, null);
    }

    @Override
    public void removeUserByNickname(String nickname) {
        removeUserByParams(null, null, nickname);
    }


    private void removeUserByParams(Long id, String email, String nickname) {
        try {
            if (id != null) {
                em.remove(em.getReference(User.class, id));
            } else {
                User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email or u.nickname = :nickname", User.class)
                        .setParameter("email", email)
                        .setParameter("nickname", nickname)
                        .getSingleResult();
                em.remove(user);
            }
        } catch (EntityNotFoundException e) {
            logger.trace("Deleting of nonexisting user, id: {},email: {},nickname: {}", id, email, nickname, e);
        }
    }
}
