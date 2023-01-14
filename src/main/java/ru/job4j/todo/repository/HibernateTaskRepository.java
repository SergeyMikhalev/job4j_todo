package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class HibernateTaskRepository implements TaskRepository {

    public static final String FIND_TASK_BY_ID = "from Task as t JOIN FETCH t.priority where t.id = :fId";
    public static final String DELETE_TASK_BY_ID = "delete Task where id = :fId";
    public static final String SET_TASK_DONE = "update Task as t set t.done = :fDone where t.id = :fId";
    public static final String UPDATE_TASK = "update Task as t set t.name = :fName, t.description = :fDescription, t.created = :fCreated, t.done = :fDone, t.priority=:fPriority where t.id = :fId";
    public static final String FIND_DONE_TASK = "from Task as t JOIN FETCH t.priority where t.done = :fDone";
    public static final String ALL_TASKS = "from Task t JOIN FETCH t.priority";

    private final CrudRepository crudRepository;

    public HibernateTaskRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query(ALL_TASKS, Task.class);
    }

    @Override
    public List<Task> finByDone(boolean done) {
        return crudRepository.query(FIND_DONE_TASK, Task.class, Map.of("fDone", done));
    }

    @Override
    public Task save(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    @Override
    public Optional<Task> findById(int taskId) {
        return crudRepository.optional(FIND_TASK_BY_ID, Task.class, Map.of("fId", taskId));
    }

    @Override
    public boolean deleteById(int taskId) {
        int result = crudRepository.update(DELETE_TASK_BY_ID, Map.of("fId", taskId));
        return result > 0;
    }

    @Override
    public boolean setTaskDone(int taskId, boolean done) {
        int result = crudRepository.update(SET_TASK_DONE, Map.of("fId", taskId, "fDone", done));
        return result > 0;
    }

    @Override
    public boolean update(Task task) {
        Map<String, Object> args = Map.of(
                "fId", task.getId(),
                "fName", task.getName(),
                "fDescription", task.getDescription(),
                "fCreated", task.getCreated(),
                "fDone", task.isDone(),
                "fPriority", task.getPriority()
        );
        int result = crudRepository.update(UPDATE_TASK, args);
        return result > 0;
    }
}
