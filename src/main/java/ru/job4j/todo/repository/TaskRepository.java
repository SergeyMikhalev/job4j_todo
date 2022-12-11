package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    List<Task> finByDone(boolean done);

    Task save(Task task);

    Optional<Task> findById(int taskId);

    boolean deleteById(int taskId);

    boolean setTaskDone(int taskId, boolean done);
}
