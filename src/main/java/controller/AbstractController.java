package controller;

import webserver.model.HttpMethod;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.getMethod();

        if (method.isGet()) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    protected abstract void doGet(HttpRequest request, HttpResponse response);

    protected abstract void doPost(HttpRequest request, HttpResponse response);
}
