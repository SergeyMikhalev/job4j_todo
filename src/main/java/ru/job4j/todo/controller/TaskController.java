package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"/", "/index"})
    public String index() {
        return "redirect:/tasks";
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        List<Task> tasks = taskService.findAll();
        System.out.println(tasks);
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @GetMapping("/filter")
    public String filtered(Model model, @RequestParam(name = "done") boolean done) {
        List<Task> tasks = taskService.findByDone(done);
        System.out.println(tasks);
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    @GetMapping("/addTask")
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

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/fullTask/{taskId}")
    public String fullTask(@PathVariable(name = "taskId") int taskId, Model model) {
        Task task = taskService.findById(taskId).get();
        model.addAttribute("modificationDisabled", true);
        model.addAttribute("task", task);
        return "fullTask";
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable(name = "taskId") int taskId) {
        taskService.deleteById(taskId);
        return "redirect:/tasks";
    }

    @GetMapping("/completeTask/{taskId}")
    public String completeTask(@PathVariable(name = "taskId") int taskId) {
        taskService.completeTask(taskId);
        return "redirect:/tasks";
    }


    @GetMapping("/editTask/{taskId}")
    public String editTask(@PathVariable(name = "taskId") int taskId, Model model) {
        Task task = taskService.findById(taskId).get();
        model.addAttribute("task", task);
        model.addAttribute("modificationDisabled", false);
        return "addTask";
    }
}
