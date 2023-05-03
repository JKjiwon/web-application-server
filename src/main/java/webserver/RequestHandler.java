package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.RequestLine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;

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
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);
            RequestLine requestLine = httpRequest.getRequestLine();

            if ("/user/create".equals(requestLine.getPath())) {
                Map<String, String> params = httpRequest.getParams();
                User user = new User(params.get("userId"),
                        params.get("password"),
                        params.get("name"),
                        params.get("email"));
                DataBase.addUser(user);
                httpResponse.senRedirect("/index.html");
            } else if ("/user/login".equals(requestLine.getPath())) {
                Map<String, String> params = httpRequest.getParams();
                User user = DataBase.findUserById(params.get("userId"));
                if (user == null) {
                    httpResponse.addHeader("Set-Cookie", "logined=" + false);
                    httpResponse.senRedirect("/user/login_failed.html");
                    return;
                }
                if (user.getPassword() != null && user.getPassword().equals(params.get("password"))) {
                    httpResponse.addHeader("Set-Cookie", "logined=" + true);
                    httpResponse.senRedirect("/index.html");
                } else {
                    httpResponse.addHeader("Set-Cookie", "logined=" + false);
                    httpResponse.senRedirect("/user/login_failed.html");
                }
            } else if ("/user/list".equals(requestLine.getPath())) {
                boolean logined = isLogin(httpRequest.getCookies().get("logined"));
                if (!logined) {
                    httpResponse.senRedirect("/user/login.html");
                    return;
                }

                Collection<User> users = DataBase.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("<table border='1'>");
                for (User user : users) {
                    sb
                            .append("<tr>")
                            .append("<td>").append(user.getUserId()).append("</td>")
                            .append("<td>").append(user.getName()).append("</td>")
                            .append("<td>").append(user.getEmail()).append("</td>")
                            .append("</tr>");
                }
                sb.append("</table>");
                httpResponse.forwardBody(sb.toString());
            } else {
                httpResponse.forward(requestLine.getPath());
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isLogin(String cookieValue) {
        if (cookieValue == null) {
            return false;
        }
        return Boolean.parseBoolean(cookieValue);
    }
}
