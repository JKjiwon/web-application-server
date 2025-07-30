package webserver;

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

    public RequestHandler(Socket connectionSocket, ControllerManager controllerManager) {
        this.connection = connectionSocket;
        this.controllerManager = controllerManager;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (connection;
             BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream output = new DataOutputStream(connection.getOutputStream())) {
            HttpRequest request = new HttpRequest(input);
            HttpResponse response = new HttpResponse(output);
            controllerManager.service(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
