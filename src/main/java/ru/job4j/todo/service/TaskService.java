package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll();

    List<Task> findByDone(boolean done);

    Task findById(int taskId);

    void deleteById(int taskId);

    void completeTask(int taskId);

    void checkAndSave(Task task, List<Integer> categoryIds);

    void checkAndUpdate(Task task, List<Integer> categoryIds);
}
