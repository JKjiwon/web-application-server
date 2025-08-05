package webserver;

import webserver.controller.DefaultController;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager {

    public final Map<String, Controller> controllerMap = new HashMap<>();

    public final DefaultController defaultController = new DefaultController();

    public void addController(String path, Controller controller) {
        controllerMap.put(path, controller);
    }

    public void service(HttpRequest request, HttpResponse response) {
        Controller controller = controllerMap.getOrDefault(request.getPath(), defaultController);
        controller.service(request, response);
    }
}
