package webserver.model;

import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public interface Controller {
    void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
