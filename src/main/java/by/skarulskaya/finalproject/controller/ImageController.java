package by.skarulskaya.finalproject.controller;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static by.skarulskaya.finalproject.controller.Parameters.IMAGE_PATH;

@WebServlet(urlPatterns = {"/uploadImage"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 25)
public class ImageController extends HttpServlet {
    private static final String CONTENT_TYPE = "image/png";
    private static String ABSOLUTE_PATH = "D:\\MusicBandWebProject\\Project\\MusicBandProject\\src\\main\\webapp\\images\\";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getParameter(IMAGE_PATH);
        String fullPath = ABSOLUTE_PATH + path;
        byte[] imageBytes = Files.readAllBytes(Paths.get(fullPath));
        response.setContentType(CONTENT_TYPE);
        response.setContentLength(imageBytes.length);
        response.getOutputStream().write(imageBytes);
    }
}