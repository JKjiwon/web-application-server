package webserver.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private static final String HTML_FILE_PATH = "." + File.separator + "webapp";

    private DataOutputStream dos;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void forward(String url) {
        try {
            byte[] body = Files.readAllBytes(new File(HTML_FILE_PATH + url).toPath());

            dos.writeBytes("HTTP/1.1 200 OK\r\n");

            if (url.endsWith(".css")) {
                dos.writeBytes("Content-Type: text/css\r\n");
            } else {
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            }

            processHeaders();

            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");

            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void forward(byte[] body) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            processHeaders();
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendRedirect(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + url + "\r\n");

            processHeaders();
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setCookie(String key, String value) {
        setCookie(key, value, "/");
    }

    public void setCookie(String key, String value, String path) {
        String cookie = key + "=" + value;

        if (path != null && !path.isEmpty()) {
            cookie += "; Path=" + path;
        }

        addHeader("Set-Cookie", cookie);
    }

    private void processHeaders() throws IOException {
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
            }
        }
    }
}
