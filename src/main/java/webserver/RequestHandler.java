package webserver;

import controller.CreateUserController;
import controller.GetUsersController;
import controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if (request.getPath().startsWith("/user/create")) {
                CreateUserController controller = new CreateUserController();
                controller.service(request, response);
            } else if (request.getPath().equals("/user/login")) {
                LoginController controller = new LoginController();
                controller.service(request, response);
            } else if (request.getPath().equals("/user/list")) {
                GetUsersController controller = new GetUsersController();
                controller.service(request, response);
            } else {
                response.forward(request.getPath());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
