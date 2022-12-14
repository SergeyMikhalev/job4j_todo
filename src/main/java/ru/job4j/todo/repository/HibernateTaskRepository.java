package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public class HibernateTaskRepository implements TaskRepository {

    public static final String FIND_TASK_BY_ID = "from Task as t where t.id = :fId";
    public static final String DELETE_TASK_BY_ID = "delete Task where id = :fId";
    public static final String SET_TASK_DONE = "update Task as t set t.done = :fDone where t.id = :fId";
    public static final String UPDATE_TASK = "update Task as t set t.name = :fName, t.description = :fDescription, t.created = :fCreated, t.done = :fDone where t.id = :fId";
    public static final String FIND_DONE_TASK = "from Task as t where t.done = :fDone";
    public static final String ALL_TASKS = "from Task";

    private final SessionFactory sf;

    public HibernateTaskRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public List<Task> findAll() {
        List<Task> result;
        try (Session session = sf.openSession()) {
            result = session.createQuery(ALL_TASKS, Task.class).list();
        }
        return result;
    }

    @Override
    public List<Task> finByDone(boolean done) {
        List<Task> result;
        try (Session session = sf.openSession()) {
            result = session.createQuery(FIND_DONE_TASK, Task.class)
                    .setParameter("fDone", done)
                    .list();
        }
        return result;
    }

    @Override
    public Task save(Task task) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        }
        return task;
    }

    @Override
    public Optional<Task> findById(int taskId) {
        Optional<Task> result;
        try (Session session = sf.openSession()) {
            result = session.createQuery(FIND_TASK_BY_ID, Task.class)
                    .setParameter("fId", taskId)
                    .uniqueResultOptional();
        }
        return result;
    }

    @Override
    public boolean deleteById(int taskId) {
        int result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery(DELETE_TASK_BY_ID)
                    .setParameter("fId", taskId)
                    .executeUpdate();
            session.getTransaction().commit();
        }
        return result > 0;
    }

    @Override
    public boolean setTaskDone(int taskId, boolean done) {
        int result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            var query = session.createQuery(SET_TASK_DONE)
                    .setParameter("fId", taskId)
                    .setParameter("fDone", done);
            System.out.println(query.toString());
            result = query.executeUpdate();
            session.getTransaction().commit();
        }
        return result > 0;
    }

    @Override
    public boolean update(Task task) {
        int result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            var query = session.createQuery(UPDATE_TASK)
                    .setParameter("fId", task.getId())
                    .setParameter("fName", task.getName())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone());
            result = query.executeUpdate();
            session.getTransaction().commit();
        }
        return result > 0;
    }
}
