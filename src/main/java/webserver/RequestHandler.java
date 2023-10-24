package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.HttpMethod;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

            if (request.getMethod().equals(HttpMethod.POST) && request.getPath().startsWith("/user/create")) {
                // request body
                String userId = request.getParameter("userId");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                User user = new User(userId, password, name, email);
                log.debug("user create {}", user);
                DataBase.addUser(user);
                response.sendRedirect("/index.html");
            } else if (request.getMethod().equals(HttpMethod.POST) && request.getPath().equals("/user/login")) {
                String userId = request.getParameter("userId");
                String password = request.getParameter("password");
                User user = DataBase.findUserById(userId);

                if (user == null || !user.getPassword().equals(password)) {
                    // 로그인 실패
                    response.setCookie("logined", "false");
                    response.sendRedirect("/user/login_failed.html");
                    return;
                }
                // 로그인 성공
                response.setCookie("logined", "true");
                response.sendRedirect("/index.html");
            } else if (request.getMethod().equals(HttpMethod.GET) && request.getPath().equals("/user/list")) {
                if (!isLogined(request)) {
                    response.sendRedirect("/index.html");
                    return;
                }
                List<User> users = new ArrayList<>(DataBase.findAll());
                byte[] body = responseUsers(users);
                response.forward(body);
            } else {
                response.forward(request.getPath());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] responseUsers(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        sb.append("<tr>");
        sb.append("<th>userId</th>");
        sb.append("<th>name</th>");
        sb.append("<th>email</th>");
        sb.append("</tr>");

        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(user.getUserId());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(user.getName());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(user.getEmail());
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString().getBytes();
    }

    private boolean isLogined(HttpRequest request) {
        String logined = request.getCookie("logined");

        if (logined == null) {
            return false;
        }

        return Boolean.parseBoolean(logined);
    }
}
