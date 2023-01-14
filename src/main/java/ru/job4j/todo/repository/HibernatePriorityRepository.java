package ru.job4j.todo.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class HibernatePriorityRepository implements PriorityRepository {

    public static final String ALL_PRIORITIES = "from Priority p";
    public static final String FIND_PRIORITY_BY_ID = "from Priority p where p.id= :fId";

    private final CrudRepository crudRepository;

    public HibernatePriorityRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public List<Priority> findAll() {
        return crudRepository.query(ALL_PRIORITIES, Priority.class);
    }

    @Override
    public Optional<Priority> findById(int priorityId) {
        return crudRepository.optional(
                FIND_PRIORITY_BY_ID,
                Priority.class,
                Map.of("fId", priorityId));
    }
}
