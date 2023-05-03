package webserver.model;

public interface Controller {
    void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
