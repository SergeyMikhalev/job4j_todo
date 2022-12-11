package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll();

    List<Task> findByDone(boolean done);

    Task save(Task task);

    Optional<Task> findById(int taskId);

    boolean deleteById(int taskId);

    boolean completeTask(int taskId);
}
