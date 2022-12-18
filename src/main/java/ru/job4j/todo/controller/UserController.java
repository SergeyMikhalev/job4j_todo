package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.User;

@Controller
public class UserController {
    @GetMapping("/registration")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User(0, "Имя", "Логин", "Пароль"));
        return "users/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user) {
        System.out.println(user);
        return "redirect:/tasks";
    }
}
