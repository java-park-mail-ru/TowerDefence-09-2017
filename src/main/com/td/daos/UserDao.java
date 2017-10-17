package com.td.daos;

import com.td.daos.inerfaces.IUserDao;
import com.td.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserDao implements IUserDao {
    private final EntityManager em;

    public UserDao(@Autowired EntityManager em) {
        this.em = em;
    }

    @Override
    public User getUserByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public boolean checkUser(String email) {
        Long count = em.createQuery("SELECT count(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count == 1;
    }

    @Override
    public boolean checkUser(Long id) {
        Long count = em.createQuery("SELECT count(u) FROM User u WHERE u.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count == 1;

    }

    @Override
    public User getUserByNickanme(String nickname) {
        return em.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
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
    public User createUser(String nickname, String email, String password) {
        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(password);
        return storeUser(user);
    }

    @Override
    public User storeUser(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public User updateUserById(Long id, String email, String login, String password) {
        User user = getUserById(id);
        user.updateEmail(email);
        user.updateNickname(login);
        user.updatePassword(password);
        return user;
    }

    @Override
    public User updateUserEmail(User user, String email) {
        user.updateEmail(email);
        return user;
    }

    @Override
    public User updateUserNickname(User user, String nickname) {
        user.updateNickname(nickname);
        return user;
    }

    @Override
    public User updateUserPassword(User user, String password) {
        user.updatePassword(password);
        return user;
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

    @Override
    public int removeUserByParams(Long id, String email, String nickname) {
        return em.createQuery("DELETE FROM User u WHERE u.id = :id or u.email = :email or u.nickname = :nickname")
                .setParameter("id", id)
                .setParameter("email", email)
                .setParameter("nickname", nickname)
                .executeUpdate();
    }


}
