package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"/", ""})
    public String tasks(Model model) {
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @GetMapping("/filter")
    public String filtered(Model model, @RequestParam(name = "done") boolean done) {
        List<Task> tasks = taskService.findByDone(done);
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @GetMapping("/add")
    public String addTaskForm(Model model) {
        Task task = new Task(1,
                "Заполните имя",
                "Заполните описание",
                LocalDateTime.now(),
                false);
        model.addAttribute("task", task);
        model.addAttribute("modificationDisabled", false);
        return "addTask";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/full/{taskId}")
    public String fullTask(@PathVariable(name = "taskId") int taskId, Model model) {
        Optional<Task> optionalTask = taskService.findById(taskId);
        if (optionalTask.isEmpty()) {
            return "redirect:/error";
        }
        model.addAttribute("modificationDisabled", true);
        model.addAttribute("task", optionalTask.get());
        return "fullTask";
    }

    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable(name = "taskId") int taskId) {
        if (!taskService.deleteById(taskId)) {
            return "redirect:/error";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/complete/{taskId}")
    public String completeTask(@PathVariable(name = "taskId") int taskId) {
        if (!taskService.completeTask(taskId)) {
            return "redirect:/error";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{taskId}")
    public String editTaskForm(@PathVariable(name = "taskId") int taskId, Model model) {
        Optional<Task> optionalTask = taskService.findById(taskId);
        if (optionalTask.isEmpty()) {
            return "redirect:/error";
        }
        model.addAttribute("task", optionalTask.get());
        model.addAttribute("modificationDisabled", false);
        return "editTask";
    }

    @PostMapping("/edit")
    public String editTask(@ModelAttribute Task task) {
        if (!taskService.update(task)) {
            return "redirect:/error";
        }
        return "redirect:/tasks";
    }
}
