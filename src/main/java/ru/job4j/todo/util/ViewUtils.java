package ru.job4j.todo.util;

import org.springframework.ui.Model;
import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

public final class ViewUtils {

    public static final String GUEST = "Гость";
    private static final String DEFAULT_TIME_ZONE = "Europe/Moscow";

    private ViewUtils() {
    }

    public static void checkUserOrSetDefault(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName(GUEST);
            user.setTimeZone(DEFAULT_TIME_ZONE);
        }
        model.addAttribute("regUser", user);
    }
}
