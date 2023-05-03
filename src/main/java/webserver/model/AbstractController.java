package webserver.model;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpMethod method = httpRequest.getRequestLine().getMethod();
        if (method.isPost()) {
            doPost(httpRequest, httpResponse);
        } else {
            doGet(httpRequest, httpResponse);
        }
    }

    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {

    }

    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }
}
