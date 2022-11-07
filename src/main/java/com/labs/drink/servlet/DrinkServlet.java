package com.labs.drink.servlet;

import com.labs.drink.dto.CreateDrinkRequest;
import com.labs.drink.dto.GetDrinkResponse;
import com.labs.drink.dto.GetDrinksResponse;
import com.labs.drink.dto.UpdateDrinkRequest;
import com.labs.drink.entity.Drink;
import com.labs.drink.service.DrinkService;
import com.labs.drink.service.KindService;
import com.labs.servlet.HttpHeaders;
import com.labs.servlet.MimeTypes;
import com.labs.servlet.ServletUtility;
import com.labs.servlet.UrlFactory;
import com.labs.user.entity.User;
import com.labs.user.service.UserService;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for handling HTTP requests considering characters operations. Servlet API does not allow named path
 * parameters so wildcard is used.
 */
@WebServlet(urlPatterns = {
        DrinkServlet.Paths.DRINKS + "/*",
        DrinkServlet.Paths.USER_DRINKS + "/*"
})
public class DrinkServlet extends HttpServlet {

    /**
     * JSON-B mapping object. According to open liberty documentation creating those is expensive. The JSON-B is only
     * one of many solutions. JSON strings can be build by hand {@link StringBuilder} or with JSON-P API. Both JSON-B
     * and JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * All characters or specified character.
         */
        public static final String DRINKS = "/api/drinks";

        /**
         * All characters belonging to the caller principal or specified character of the caller principal.
         */
        public static final String USER_DRINKS = "/api/user/drinks";

    }

    /**
     * Definition of regular expression patterns supported by this servlet. Separate inner class provides composition
     * for static fields. Whereas servlet activation path can be compared to {@link Paths} the path info (denoted by
     * wildcard in paths) can be compared using regular expressions.
     */
    public static class Patterns {

        /**
         * All characters.
         */
        public static final String DRINKS = "^/?$";

        /**
         * Specified character.
         */
        public static final String DRINK = "^/[0-9]+/?$";

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.DRINKS.equals(servletPath)) {
            if (path.matches(Patterns.DRINK)) {
                getDrink(request, response);
                return;
            } else if (path.matches(Patterns.DRINKS)) {
                getDrinks(request, response);
                return;
            }
        } else if (Paths.USER_DRINKS.equals(servletPath)) {
            if (path.matches(Patterns.DRINK)) {
                getUserDrink(request, response);
                return;
            } else if (path.matches(Patterns.DRINK)) {
                getUserDrinks(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_DRINKS.equals(request.getServletPath())) {
            if (path.matches(Patterns.DRINK)) {
                postUserDrink(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_DRINKS.equals(request.getServletPath())) {
            if (path.matches(Patterns.DRINK)) {
                putUserDrink(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_DRINKS.equals(request.getServletPath())) {
            if (path.matches(Patterns.DRINK)) {
                deleteUserDrink(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Sends single character or 404 error if not found.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getDrink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DrinkService service = (DrinkService) request.getServletContext().getAttribute("drinkService");

        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Drink> drink = service.find(id);

        if (drink.isPresent()) {
            response.setContentType(MimeTypes.APPLICATION_JSON);
            response.getWriter()
                    .write(jsonb.toJson(GetDrinkResponse.entityToDtoMapper().apply(drink.get())));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Sends all characters as JSON.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void getDrinks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DrinkService service = (DrinkService) request.getServletContext().getAttribute("drinkService");

        response.setContentType(MimeTypes.APPLICATION_JSON);
        response.getWriter()
                .write(jsonb.toJson(GetDrinksResponse.entityToDtoMapper().apply(service.findAll())));
    }

    /**
     * Sends single caller principal's character or 404 error if not found.  Caller principal should be stored in
     * session context.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getUserDrink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DrinkService drinkService = (DrinkService) request.getServletContext().getAttribute("drinkService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            //Parsed request path is valid with character pattern and can contain starting and ending '/'.
            Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
            Optional<Drink> drink = drinkService.find(user.get(), id);

            if (drink.isPresent()) {
                response.setContentType(MimeTypes.APPLICATION_JSON);
                response.getWriter()
                        .write(jsonb.toJson(GetDrinkResponse.entityToDtoMapper().apply(drink.get())));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

    /**
     * Sends all call principal's characters as JSON. Caller principal should be stored in session context.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void getUserDrinks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DrinkService drinkService = (DrinkService) request.getServletContext().getAttribute("drinkService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            response.setContentType(MimeTypes.APPLICATION_JSON);
            response.getWriter()
                    .write(jsonb.toJson(GetDrinksResponse.entityToDtoMapper()
                            .apply(drinkService.findAll(user.get()))));
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

    /**
     * Decodes JSON request and stores new character.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void postUserDrink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DrinkService drinkService = (DrinkService) request.getServletContext().getAttribute("drinkService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        KindService kindService = (KindService) request.getServletContext().getAttribute("kindService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            CreateDrinkRequest requestBody = jsonb.fromJson(request.getInputStream(), CreateDrinkRequest.class);

            Drink drink = CreateDrinkRequest
                    .dtoToEntityMapper(name -> kindService.find(name).orElse(null), user::get)
                    .apply(requestBody);

            try {
                drinkService.create(drink);
                //When creating new resource, its location should be returned.
                response.addHeader(HttpHeaders.LOCATION,
                        UrlFactory.createUrl(request, Paths.USER_DRINKS, drink.getId().toString()));
                //When creating new resource, appropriate code should be set.
                response.setStatus(HttpServletResponse.SC_CREATED);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

    /**
     * Decodes JSON request and updates existing character.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void putUserDrink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DrinkService drinkService = (DrinkService) request.getServletContext().getAttribute("drinkService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            //Parsed request path is valid with character pattern and can contain starting and ending '/'.
            Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
            Optional<Drink> drink = drinkService.find(user.get(), id);

            if (drink.isPresent()) {
                UpdateDrinkRequest requestBody = jsonb.fromJson(request.getInputStream(),
                        UpdateDrinkRequest.class);

                UpdateDrinkRequest.dtoToEntityUpdater().apply(drink.get(), requestBody);

                drinkService.update(drink.get());
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

    /**
     * Deletes existing character denoted by path param.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void deleteUserDrink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DrinkService drinkService = (DrinkService) request.getServletContext().getAttribute("drinkService");
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        String principal = (String) request.getSession().getAttribute("principal");

        Optional<User> user = userService.find(principal);

        if (user.isPresent()) {
            //Parsed request path is valid with character pattern and can contain starting and ending '/'.
            Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
            Optional<Drink> drink = drinkService.find(user.get(), id);

            if (drink.isPresent()) {
                drinkService.delete(drink.get().getId());
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            //This should not happen as servlet is after authorization filter.
            throw new IllegalStateException(String.format("No user %s found", principal));
        }
    }

}
