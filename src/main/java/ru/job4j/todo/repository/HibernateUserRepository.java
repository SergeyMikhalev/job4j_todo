package ru.job4j.todo.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
public class HibernateUserRepository implements UserRepository {

    public static final String FIND_USER_BY_LOGIN_AND_PASSWORD = "from User where login =:fLogin and password = :fPassword";

    private final CrudRepository crudRepository;

    public HibernateUserRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(FIND_USER_BY_LOGIN_AND_PASSWORD, User.class,
                Map.of("fLogin", login, "fPassword", password));
    }
}
