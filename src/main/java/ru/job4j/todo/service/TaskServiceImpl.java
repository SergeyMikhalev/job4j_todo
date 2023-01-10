package ru.job4j.todo.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByDone(boolean done) {
        return taskRepository.finByDone(done);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(int taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public boolean deleteById(int taskId) {
        return taskRepository.deleteById(taskId);
    }

    @Override
    public boolean completeTask(int taskId) {
        return taskRepository.setTaskDone(taskId, true);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }
}
