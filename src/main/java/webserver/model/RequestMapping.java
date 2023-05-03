package webserver.model;

import webserver.controller.ForwardController;
import webserver.controller.UserCreateController;
import webserver.controller.UserListController;
import webserver.controller.UserLoginController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/user/create", new UserCreateController());
        controllers.put("/user/login", new UserLoginController());
        controllers.put("/user/list", new UserListController());
    }

    public static Controller getController(String path) {
        return controllers.getOrDefault(path, new ForwardController());
    }
}
