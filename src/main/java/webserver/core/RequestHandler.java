package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ControllerManager controllerManager;
    private final HttpSessionManager httpSessionManager;

    public RequestHandler(Socket connectionSocket, ControllerManager controllerManager, HttpSessionManager httpSessionManager) {
        this.connection = connectionSocket;
        this.controllerManager = controllerManager;
        this.httpSessionManager = httpSessionManager;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (connection;
             BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream output = new DataOutputStream(connection.getOutputStream())) {
            HttpRequest request = new HttpRequest(input, httpSessionManager);
            HttpResponse response = new HttpResponse(output);
            String sessionId = HttpSessionUtils.getSessionId(request);
            response.addHeader("Set-Cookie", HttpSessionUtils.HTTP_SESSION_ID_KEY + "=" + sessionId + "; Path=/");
            controllerManager.service(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
