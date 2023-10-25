package controller;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
