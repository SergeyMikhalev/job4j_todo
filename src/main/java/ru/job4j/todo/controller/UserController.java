package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegistrationForm(Model model) {
        User user = new User();
        user.setName("Введите имя");
        user.setLogin("Введите логин");
        user.setPassword("Введите пароль");
        model.addAttribute("user", user);
        model.addAttribute("fail", false);
        return "users/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user, Model model) {
        String resultTemplate = "redirect:/tasks";
        Optional<User> savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("fail", true);
            resultTemplate = "users/registration";
        }
        return resultTemplate;
    }

    @GetMapping("/login")
    public String getLoginForm(Model model, @RequestParam(name = "fail", required = false) boolean fail) {
        model.addAttribute("fail", fail);
        return "users/login";
    }

    @PostMapping("/login")
    public String login(Model model, @ModelAttribute User user, HttpSession session) {
        Optional<User> dbUser = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (dbUser.isEmpty()) {
            return "redirect:/login?fail=true";
        }
        session.setAttribute("user", dbUser.get());
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
