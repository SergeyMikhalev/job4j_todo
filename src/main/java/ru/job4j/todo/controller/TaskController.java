package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.job4j.todo.util.ViewUtils.checkUserOrSetDefault;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"/", ""})
    public String tasks(Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/filter")
    public String filtered(Model model, @RequestParam(name = "done") boolean done, HttpSession session) {
        checkUserOrSetDefault(model, session);
        List<Task> tasks = taskService.findByDone(done);
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/add")
    public String addTaskForm(Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        Task task = new Task(1,
                "Заполните имя",
                "Заполните описание",
                LocalDateTime.now(),
                false);
        model.addAttribute("task", task);
        model.addAttribute("modificationDisabled", false);
        return "tasks/add";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/full/{id}")
    public String fullTask(@PathVariable(name = "id") int id, Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isEmpty()) {
            System.out.println(" -- there is no such task --");
            return "redirect:/error1001";
        }
        model.addAttribute("modificationDisabled", true);
        model.addAttribute("task", optionalTask.get());
        return "tasks/full";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable(name = "id") int id) {
        if (!taskService.deleteById(id)) {
            return "redirect:/error1001";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/complete/{id}")
    public String completeTask(@PathVariable(name = "id") int id, Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        if (!taskService.completeTask(id)) {
            return "redirect:/error1001";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable(name = "id") int id, Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isEmpty()) {
            return "redirect:/error1001";
        }
        model.addAttribute("task", optionalTask.get());
        model.addAttribute("modificationDisabled", false);
        return "tasks/edit";
    }

    @PostMapping("/edit")
    public String editTask(@ModelAttribute Task task) {
        if (!taskService.update(task)) {
            return "redirect:/error1001";
        }
        return "redirect:/tasks";
    }
}
