package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static ru.job4j.todo.util.ViewUtils.checkUserOrSetDefault;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    public TaskController(TaskService taskService, PriorityService priorityService, CategoryService categoryService) {
        this.taskService = taskService;
        this.priorityService = priorityService;
        this.categoryService = categoryService;
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
        Task task = new Task();
        task.setName("Заполните поле");
        task.setDescription("Заполните поле");
        task.setCreated(LocalDateTime.now());

        fillTaskForm(model, task, false);
        return "tasks/add";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task,
                          @RequestParam(value = "categoryIds", required = false, defaultValue = "") List<Integer> categoryIds,
                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        task.setUser(user);
        taskService.checkAndSave(task, categoryIds);
        return "redirect:/tasks";
    }

    @GetMapping("/full/{id}")
    public String fullTask(@PathVariable(name = "id") int id, Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        Task task = taskService.findById(id);
        fillTaskForm(model, task, true);
        return "tasks/full";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable(name = "id") int id) {
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/complete/{id}")
    public String completeTask(@PathVariable(name = "id") int id, Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        taskService.completeTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable(name = "id") int id, Model model, HttpSession session) {
        checkUserOrSetDefault(model, session);
        Task task = taskService.findById(id);
        fillTaskForm(model, task, false);
        return "tasks/edit";
    }

    @PostMapping("/edit")
    public String editTask(@ModelAttribute Task task,
                           @RequestParam(value = "categoryIds", required = false, defaultValue = "") List<Integer> categoryIds) {
        taskService.checkAndUpdate(task, categoryIds);
        return "redirect:/tasks";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String onNoSuchElementRedirection(NoSuchElementException e, Model model) {
        model.addAttribute("errorMsg", e.getMessage());
        return "shared/error";
    }

    private void fillTaskForm(Model model, Task task, boolean modificationDisabled) {
        model.addAttribute("task", task);
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("allCategories", categoryService.findAll());
        model.addAttribute("modificationDisabled", modificationDisabled);
    }


}
