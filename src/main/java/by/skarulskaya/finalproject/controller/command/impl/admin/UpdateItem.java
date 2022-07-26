package by.skarulskaya.finalproject.controller.command.impl.admin;

import by.skarulskaya.finalproject.controller.Router;
import by.skarulskaya.finalproject.controller.command.Command;
import by.skarulskaya.finalproject.exception.CommandException;
import by.skarulskaya.finalproject.exception.ServiceException;
import by.skarulskaya.finalproject.model.entity.Item;
import by.skarulskaya.finalproject.model.service.impl.ItemService;
import by.skarulskaya.finalproject.validator.impl.BaseValidatorImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static by.skarulskaya.finalproject.controller.PagesPaths.*;
import static by.skarulskaya.finalproject.controller.Parameters.*;
import static by.skarulskaya.finalproject.controller.ParametersMessages.*;

public class UpdateItem implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static String ABSOLUTE_PATH = "D:\\MusicBandWebProject\\Project\\MusicBandProject\\src\\main\\webapp\\images\\";
    private static final ItemService itemService = ItemService.getInstance();

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Router router = new Router();
        String currentPage = (String) request.getSession().getAttribute(CURRENT_PAGE);
        String itemIdParameter = request.getParameter(ITEM_ID);
        HashMap<String, String> map = new HashMap<>(4);
        map.put(ITEM_NAME, request.getParameter(ITEM_NAME));
        map.put(ITEM_PRICE, request.getParameter(ITEM_PRICE));
        map.put(ITEM_DESCRIPTION, request.getParameter(ITEM_DESCRIPTION));
        map.put(ITEM_ID, itemIdParameter);
        try {
            List<Integer> sizesId = getIdFromJson(request.getParameter(SIZES_ID));
            List<Integer> categoriesId = getIdFromJson(request.getParameter(CATEGORIES_ID));
            List<String> amountsInStock = new ArrayList<>(10);
            for(int sizeId: sizesId) {
                String amountInStock = request.getParameter(ITEM_SIZE_AMOUNT_IN_STOCK + sizeId);
                amountsInStock.add(amountInStock);
            }
            if(itemIdParameter != null && !BaseValidatorImpl.INSTANCE.validateId(itemIdParameter)) {
                router.setCurrentPage(ERROR_404);
                return router;
            }
            if(itemService.updateOrCreate(map, sizesId, amountsInStock, categoriesId)
                    && updateItemPicturePath(request, map)) {
                router.setCurrentType(Router.Type.REDIRECT);
                router.setCurrentPage(request.getContextPath() + currentPage);
                return router;
            }
            for(String key: map.keySet()) {
                String value = map.get(key);
                if (value == null) {
                    continue;
                }
                switch (value) {
                    case INVALID_NAME -> {
                        request.setAttribute(INVALID_NAME, INVALID_ITEM_NAME_MESSAGE);
                        map.put(key, null);
                    }
                    case INVALID_PRICE-> {
                        request.setAttribute(INVALID_PRICE, INVALID_PRICE_MESSAGE);
                        map.put(key, null);
                    }
                    case INVALID_DESCRIPTION-> {
                        request.setAttribute(INVALID_DESCRIPTION, INVALID_DESCRIPTION_MESSAGE);
                        map.put(key, null);
                    }
                    case INVALID_ITEM_SIZES-> {
                        request.setAttribute(INVALID_ITEM_SIZES, INVALID_ITEM_SIZES_MESSAGE);
                        map.put(key, null);
                    }
                    case INVALID_AMOUNTS_IN_STOCK-> {
                        request.setAttribute(INVALID_AMOUNTS_IN_STOCK, INVALID_AMOUNTS_IN_STOCK_MESSAGE);
                        map.put(key, null);
                    }
                    case INVALID_CATEGORIES-> {
                        request.setAttribute(INVALID_CATEGORIES, INVALID_CATEGORIES_CATEGORIES);
                        map.put(key, null);
                    }
                    case NOT_UNIQUE_ITEM_NAME-> {
                        request.setAttribute(INVALID_NAME, NOT_UNIQUE_ITEM_NAME_MESSAGE);
                        map.put(key, null);
                    }
                    case NOT_UNIQUE_IMAGE_NAME-> {
                        request.setAttribute(INVALID_IMAGE, NOT_UNIQUE_IMAGE_NAME_MESSAGE);
                        map.put(key, null);
                    }
                }
            }
            request.setAttribute(ITEM_DATA_MAP, map);
            router.setCurrentPage(currentPage);
            return router;
        } catch (NumberFormatException | ParseException | ServiceException e) {
            throw new CommandException(e);
        }
    }

    private boolean updateItemPicturePath(HttpServletRequest request, HashMap<String, String> dataMap) throws CommandException {
        try (InputStream inputStream = request.getPart(IMAGE_PATH).getInputStream()) {
            String fileName = request.getPart(IMAGE_PATH).getSubmittedFileName();
            if (fileName.isBlank()) {
                return true;
            }
            String fullPath = ABSOLUTE_PATH + fileName;
            Path imagePath = new File(fullPath).toPath();
            File target = new File(fullPath);
            if (target.exists()) {
                dataMap.put(INVALID_IMAGE, NOT_UNIQUE_IMAGE_NAME);
                return false;
            }
            Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
            int id = Integer.parseInt(dataMap.get(ITEM_ID));
            if (!itemService.updateItemImagePath(id, fileName)) {
                throw new CommandException("Failed to update item picture path");
            }
            return true;
        } catch (ServletException | ServiceException | IOException e) {
            throw new CommandException(e);
        }
    }

    private List<Integer> getIdFromJson(String json) throws ParseException, NumberFormatException {
        List<Integer> ids = new ArrayList<>(10);
        Object obj = new JSONParser().parse(json);
        JSONArray jArray = (JSONArray) obj;
        for (Object o : jArray) {
            int id = Integer.parseInt((String) o);
            ids.add(id);
        }
        return ids;
    }
}