package webserver.controller;

import webserver.core.AbstractController;
import webserver.core.HttpRequest;
import webserver.core.HttpResponse;

public class DefaultController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String forwardPath = request.getPath().equals("/") ? "/index.html" : request.getPath();
        response.forward(forwardPath);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }
}
