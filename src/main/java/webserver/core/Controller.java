package webserver.core;

public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
