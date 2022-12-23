package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
public class HibernateUserRepository implements UserRepository {

    private final SessionFactory sf;

    public HibernateUserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public Optional<User> save(User user) {
        User result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            result = user;
        } catch (Exception e) {
            result = null;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> result;
        try (Session session = sf.openSession()) {
            result = session.createQuery("from User where login =:fLogin and password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password)
                    .uniqueResultOptional();
        }
        return result;
    }
}
