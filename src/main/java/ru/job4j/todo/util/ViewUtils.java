package ru.job4j.todo.util;

import org.springframework.ui.Model;
import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

public final class ViewUtils {

    public static final String GUEST = "Гость";

    private ViewUtils() {
    }

    public static void checkUserOrSetDefault(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User(0, GUEST, null, null);
        }
        model.addAttribute("regUser", user);
    }
}
