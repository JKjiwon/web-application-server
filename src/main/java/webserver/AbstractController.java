package webserver;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if (request.isPostMethod()) {
            doPost(request, response);
        } else {
            doGet(request, response);
        }
    }

    public abstract void doGet(HttpRequest request, HttpResponse response);

    public abstract void doPost(HttpRequest request, HttpResponse response);
}
