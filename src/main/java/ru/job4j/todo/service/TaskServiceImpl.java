package ru.job4j.todo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.CategoryRepository;
import ru.job4j.todo.repository.PriorityRepository;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PriorityRepository priorityRepository;
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(TaskRepository taskRepository,
                           PriorityRepository priorityRepository,
                           CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.priorityRepository = priorityRepository;
        this.categoryRepository = categoryRepository;
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
    public Task findById(int taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            String errorMsg = "Невозможно найти задачу. Задачи с id = " + taskId + "нет в БД";
            logger.error(errorMsg);
            throw new NoSuchElementException(errorMsg);
        }
        return task.get();
    }

    @Override
    public void deleteById(int taskId) {
        if (!taskRepository.deleteById(taskId)) {
            String errorMsg = "Не удалось удалить задачу. Задачи с id = " + taskId + "нет в БД";
            logger.error(errorMsg);
            throw new NoSuchElementException(errorMsg);
        }
    }

    @Override
    public void completeTask(int taskId) {
        if (!taskRepository.setTaskDone(taskId, true)) {
            String errorMsg = "Не удалось завершить задачу. Задачи с id = " + taskId + "нет в БД";
            logger.error(errorMsg);
            throw new NoSuchElementException(errorMsg);
        }
    }


    @Override
    public void checkAndSave(Task task, List<Integer> categoryIds) {
        task.setPriority(checkPriority(task));
        task.setCategories(checkCategories(categoryIds));
        save(task);
    }

    @Override
    public void checkAndUpdate(Task task, List<Integer> categoryIds) {
        task.setPriority(checkPriority(task));
        task.setCategories(checkCategories(categoryIds));
        update(task);
    }

    private boolean update(Task task) {
        return taskRepository.update(task);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    private List<Category> checkCategories(List<Integer> categoryIds) {
        List<Category> categories = categoryRepository.findIdsIn(categoryIds);
        if (categories.size() != categoryIds.size()) {
            String errorMsg = "Невозможно сохранить задачу. Некоторые из id = " + categoryIds + "отсутствуют в БД";
            logger.error(errorMsg);
            throw new NoSuchElementException(errorMsg);
        }
        return categories;
    }

    private Priority checkPriority(Task task) {
        int priorityId = task.getPriority().getId();
        Optional<Priority> priority = priorityRepository.findById(priorityId);
        return priority.orElseThrow(() -> {
            String errorMsg = "Невозможно сохранить задачу. Приоритета с id ="
                    + priorityId + "нет в БД";
            logger.error(errorMsg);
            return new NoSuchElementException(errorMsg);
        });
    }
}
