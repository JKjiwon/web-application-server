package webserver.model;

import controller.Controller;
import controller.CreateUserController;
import controller.GetUsersController;
import controller.LoginController;

import java.util.HashMap;
import java.util.Map;

public final class RequestMapping {

    private static Map<String, Controller> router = new HashMap<>();

    static {
        router.put("/user/create", new CreateUserController());
        router.put("/user/login", new LoginController());
        router.put("/user/list", new GetUsersController());
    }

    public static Controller getController(String requestUrl) {
        return router.get(requestUrl);
    }
}
