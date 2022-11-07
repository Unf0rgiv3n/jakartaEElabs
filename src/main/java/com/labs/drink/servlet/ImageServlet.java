package com.labs.drink.servlet;

import com.labs.drink.entity.Drink;
import com.labs.drink.service.DrinkService;
import com.labs.servlet.HttpHeaders;
import com.labs.servlet.MimeTypes;
import com.labs.servlet.ServletUtility;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for serving and uploading characters' portraits i raster image format.
 */
@WebServlet(urlPatterns = ImageServlet.Paths.IMAGES + "/*")
@MultipartConfig(maxFileSize = 200 * 1024)
public class ImageServlet extends HttpServlet {

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * Specified portrait for download and upload.
         */
        public static final String IMAGES = "/api/images";

    }

    /**
     * Definition of regular expression patterns supported by this servlet. Separate inner class provides composition
     * for static fields. Whereas servlet activation path can be compared to {@link Paths} the path info (denoted by
     * wildcard in paths) can be compared using regular expressions.
     */
    public static class Patterns {

        /**
         * Specified portrait (for download).
         */
        public static final String IMAGE = "^/[0-9]+/?$";

    }

    /**
     * Request parameters (both query params and request parts) which can be sent by the client.
     */
    public static class Parameters {

        /**
         * Portrait image part.
         */
        public static final String IMAGE = "image";

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.IMAGES.equals(servletPath)) {
            if (path.matches(Patterns.IMAGE)) {
                getImage(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.IMAGES.equals(servletPath)) {
            if (path.matches(Patterns.IMAGE)) {
                putImage(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Updates character's portrait. Receives portrait bytes from request and stores them in the data storage.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException      if any input or output exception occurred
     * @throws ServletException if this request is not of type multipart/form-data
     */
    private void putImage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        DrinkService service = (DrinkService) request.getServletContext().getAttribute("drinkService");

        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Drink> drink = service.find(id);

        if (drink.isPresent()) {
            Part image = request.getPart(Parameters.IMAGE);
            if (image != null) {
                service.updatePortrait(id, image.getInputStream());
            }
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Fetches portrait as byte array from data storage and sends is through http protocol.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DrinkService service = (DrinkService) request.getServletContext().getAttribute("drinkService");

        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Drink> drink = service.find(id);

        if (drink.isPresent()) {
            //Type should be stored in the database but in this project we assume everything to be png.
            response.addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.IMAGE_PNG);
            response.setContentLength(drink.get().getImage().length);
            response.getOutputStream().write(drink.get().getImage());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}

