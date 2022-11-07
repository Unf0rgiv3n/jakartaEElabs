package com.labs.user.servlet;

import com.labs.servlet.MimeTypes;
import com.labs.servlet.ServletUtility;
import com.labs.user.entity.User;
import com.labs.user.service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = UserAvatarServlet.Paths.AVATARS + "/*")
@MultipartConfig(maxFileSize = 200 * 1024)
public class UserAvatarServlet extends HttpServlet {
    private UserService userService;

    @Inject
    public UserAvatarServlet(UserService userService) {
        this.userService = userService;
    }

    public static class Paths {
        public static final String AVATARS = "/api/avatars";
    }

    public static class Patterns {
        public static final String AVATAR = "^/[a-zA-Z0-9]+/?$";
    }

    static class Parameters {
        static final String AVATAR = "avatar";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.AVATARS.equals(servletPath)) {
            if (path.matches(Patterns.AVATAR)) {
                getAvatar(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void getAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String path = ServletUtility.parseRequestPath(request);
        String login = path.replaceAll("/", "");
        Optional<User> user = userService.find(login);
        System.out.println(user.get().isHaveAvatar());
        System.out.println(user.get().getAvatar());
        if (user.isPresent()) {
            if(!user.get().isHaveAvatar()){
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            byte[] avatar = user.get().getAvatar();
            response.addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.IMAGE_PNG);
            response.setContentLength(avatar.length);
            response.getOutputStream().write(avatar);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();

        if (Paths.AVATARS.equals(servletPath) && path.matches(Patterns.AVATAR)) {
            deleteAvatar(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void deleteAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ServletUtility.parseRequestPath(request);
        String login = path.replaceAll("/", "");
        Optional<User> user = userService.find(login);
        if(user.isPresent()) {
            System.out.println(user.get().getLogin());
            userService.deleteAvatar(user.get());
            //System.out.println(user.get().isHaveAvatar());
            System.out.println(user.get().getAvatar());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();

        if (Paths.AVATARS.equals(servletPath) && path.matches(Patterns.AVATAR)) {
            putAvatar(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void putAvatar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = ServletUtility.parseRequestPath(request);
        String login = path.replaceAll("/", "");
        Optional<User> user = userService.find(login);

        if (user.isPresent()) {
            userService.updateAvatar(user.get(), request.getPart(Parameters.AVATAR));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}