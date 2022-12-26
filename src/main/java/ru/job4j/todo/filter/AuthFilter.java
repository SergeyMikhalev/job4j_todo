package ru.job4j.todo.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class AuthFilter implements Filter {

    private static final Set<String> ALLOWED_UNREGISTERED =
            Set.of("registration", "login", "index", "tasks",
                    "registration/", "login/", "index/", "tasks/");
    public static final String LOGIN_PAGE = "/login";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        System.out.println(uri);
        if (uriAllowedForUnregistered(uri) || "/".equals(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + LOGIN_PAGE);
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean uriAllowedForUnregistered(String uri) {
        return ALLOWED_UNREGISTERED.stream().anyMatch(uri::endsWith);
    }
}
