package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream output = new DataOutputStream(connection.getOutputStream())) {
            String requestLine = input.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                throw new IOException("No request line received");
            }
            String path = extractPath(requestLine);
            log.info("Path {}", path);

            Map<String, String> headers = new HashMap<>();
            String headerLine = null;
            while (!(headerLine = input.readLine()).isEmpty()) {
                String[] parts = headerLine.split(":");
                headers.put(parts[0].trim(), parts[1].trim());
                log.debug("Header {}", headerLine);
            }

            if (path.equals("/index.html")) {
                writeHtml(Files.readAllBytes(Path.of("./webapp" + path)), output);
            } else if (path.equals("/user/form.html")) {
                writeHtml(Files.readAllBytes(Path.of("./webapp" + path)), output);
            } else if (path.startsWith("/user/create")) {
                String requestBody = readRequestBody(headers, input);
                log.info("Request Body {}", requestBody);
                User user = createUser(requestBody);
                DataBase.addUser(user);
                log.info("User created! {}", user);
            } else {
                writeHtml("Hello World".getBytes(UTF_8), output);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String extractPath(String requestLine) {
        String[] parts = requestLine.split(" ");
        return parts[1];
    }

    private void writeHtml(byte[] body, DataOutputStream output) throws IOException {
        response200Header(output, body.length);
        responseBody(output, body);
    }

    private String readRequestBody(Map<String, String> headers, BufferedReader input) throws IOException {
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        return IOUtils.readData(input, contentLength);
    }

    private User createUser(String requestBody) {
        Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(requestBody);
        String userId = URLDecoder.decode(queryStringMap.getOrDefault("userId", ""), UTF_8);
        String password = URLDecoder.decode(queryStringMap.getOrDefault("password", ""), UTF_8);
        String name = URLDecoder.decode(queryStringMap.getOrDefault("name", ""), UTF_8);
        String email = URLDecoder.decode(queryStringMap.getOrDefault("email", ""), UTF_8);
        return new User(userId, password, name, email);
    }

    private void response200Header(DataOutputStream output, int lengthOfBodyContent) {
        try {
            output.writeBytes("HTTP/1.1 200 OK\r\n");
            output.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            output.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            output.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream output, byte[] body) {
        try {
            output.write(body);
            output.writeBytes("\r\n");
            output.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
