package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.AbstractController;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public class ForwardController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        String path = httpRequest.getRequestLine().getPath();
        log.info("call path [{}]", path);
        httpResponse.forward(path);
    }
}
