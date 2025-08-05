package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final DataOutputStream dos;
    private final Map<String, String> headers = new HashMap<>();

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void forward(String path) {
        try {
            byte[] contents = Files.readAllBytes(Path.of("./webapp" + path));
            headers.put("Content-Type", extractContentType(path));
            headers.put("Content-Length", String.valueOf(contents.length));
            response200Header();
            responseBody(contents);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void forwardBody(String body) {
        byte[] contents = body.getBytes(StandardCharsets.UTF_8);
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", String.valueOf(contents.length));
        response200Header();
        responseBody(contents);
    }

    public void sendRedirect(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            processHeaders();
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String extractContentType(String path) {
        if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "application/javascript";
        } else {
            return "text/html;charset=utf-8";
        }
    }

    private void response200Header() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHeaders() throws IOException {
        for (String key : headers.keySet()) {
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
        }
    }
}
