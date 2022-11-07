package com.labs.user.servlet;

import com.labs.servlet.MimeTypes;
import com.labs.servlet.ServletUtility;
import com.labs.user.dto.GetUserResponse;
import com.labs.user.dto.GetUsersResponse;
import com.labs.user.entity.User;
import com.labs.user.service.UserService;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {
        UserServlet.Paths.USERS + "/*"
})
public class UserServlet extends HttpServlet {

    private UserService userService;

    @Inject
    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    public static class Paths {
        public static final String USERS = "/api/users";
    }

    public static class Patterns {
        public static final String USERS = "^/?$";
        public static final String USER = "^/[a-zA-Z0-9]+/?$";
    }

    /**
     * JSON-B mapping object. According to open liberty documentation creating those is expensive. The JSON-B is only
     * one of many solutions. JSON strings can be build by hand {@link StringBuilder} or with JSON-P API. Both JSON-B
     * and JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        UserService service = (UserService) request.getServletContext().getAttribute("userService");
//        String principal = (String) request.getSession().getAttribute("principal");
//        response.setContentType(MimeTypes.APPLICATION_JSON);
//        if (principal != null) {
//            Optional<User> user = service.find(principal);
//            if (user.isPresent()) {
//                response.getWriter().write(jsonb.toJson(GetUserResponse.entityToDtoMapper().apply(user.get())));
//                return;
//            }
//        }
//        response.getWriter().write("{}");//Empty JSON object.

        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.USERS.equals(servletPath)) {
            if (path.matches(Patterns.USER)) {
                getUser(request, response);
                return;
            } else if (path.matches(Patterns.USERS)) {
                getUsers(request, response);
                return;
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Optional<User> user = userService.find(login);
        if (user.isPresent()) {
            response.setContentType(MimeTypes.APPLICATION_JSON);
            response.getWriter()
                    .write(jsonb.toJson(GetUserResponse.entityToDtoMapper().apply(user.get())));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypes.APPLICATION_JSON);
        response.getWriter()
                .write(jsonb.toJson(GetUsersResponse.entityToDtoMapper().apply(userService.findAll())));
    }
}
