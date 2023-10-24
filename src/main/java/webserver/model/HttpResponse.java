package webserver.model;

import java.io.OutputStream;

public class HttpResponse {

    public HttpResponse(OutputStream out) {

    }

    public void forward(String url) {
    }

    public void sendRedirect(String url) {

    }

    public void addHeader(String key, String value) {

    }

    public void setCookie(String key, String value) {
        setCookie(key, value, "/");
    }

    public void setCookie(String key, String value, String path) {
        String cookie = key + "=" + value;

        if (path != null && !path.isEmpty()) {
            cookie += ";Path=" + path;
        }

        addHeader("Set-Cookie", cookie);
    }
}
