package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

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
            String path = extractPath(requestLine);
            byte[] body;
            if (path.equals("/index.html")) {
                body = Files.readAllBytes(Path.of("./webapp" + path));
            } else {
                body = "Hello World".getBytes(UTF_8);
            }
            response200Header(output, body.length);
            responseBody(output, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String extractPath(String requestLine) {
        String[] parts = requestLine.split(" ");
        return parts[1];
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
