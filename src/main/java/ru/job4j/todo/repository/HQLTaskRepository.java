package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public class HQLTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    public HQLTaskRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public List<Task> findAll() {
        Session session = sf.openSession();
        List<Task> result;
        try (session) {
            result = session.createQuery("from Task", Task.class).list();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return result;
    }

    @Override
    public List<Task> finByDone(boolean done) {
        Session session = sf.openSession();
        List<Task> result;
        try (session) {
            result = session.createQuery("from Task as t where t.done = :fDone", Task.class)
                    .setParameter("fDone", done)
                    .list();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return result;
    }

    @Override
    public Task save(Task task) {
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return task;
    }

    @Override
    public Optional<Task> findById(int taskId) {
        Session session = sf.openSession();
        Optional<Task> result;
        try (session) {
            result = session.createQuery("from Task as t where t.id = :fId", Task.class)
                    .setParameter("fId", taskId)
                    .uniqueResultOptional();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return result;
    }

    @Override
    public boolean deleteById(int taskId) {
        Session session = sf.openSession();
        int result;
        try (session) {
            session.beginTransaction();
            result = session.createQuery("delete Task where id = :fId")
                    .setParameter("fId", taskId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return result > 0;
    }

    @Override
    public boolean setTaskDone(int taskId, boolean done) {
        Session session = sf.openSession();
        int result;
        try (session) {
            session.beginTransaction();
            var query = session.createQuery("update Task as t set t.done = :fDone where t.id = :fId")
                    .setParameter("fId", taskId)
                    .setParameter("fDone", done);
            System.out.println(query.toString());
            result = query.executeUpdate();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return result > 0;
    }
}
