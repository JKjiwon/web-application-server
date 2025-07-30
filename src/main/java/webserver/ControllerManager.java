package webserver;

import webserver.controller.DefaultController;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager {

    public final Map<String, AbstractController> controllerMap = new HashMap<>();

    public final DefaultController defaultController = new DefaultController();

    public void addController(String path, AbstractController controller) {
        controllerMap.put(path, controller);
    }

    public void service(HttpRequest request, HttpResponse response) {
        AbstractController controller = controllerMap.getOrDefault(request.getPath(), defaultController);
        controller.service(request, response);
    }
}
