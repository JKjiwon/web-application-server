package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

        try (connection;
             BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream output = new DataOutputStream(connection.getOutputStream())) {

            HttpRequest request = new HttpRequest(input);
            HttpResponse response = new HttpResponse(output);

            if (request.isPostMethod() && request.getPath().equals("/user/create")) {
                User user = createUser(request);
                DataBase.addUser(user);
                log.info("User created! {}", user);
                response.sendRedirect("/index.html");
            } else if (request.isPostMethod() && request.getPath().equals("/user/login")) {
                boolean isLogin = login(request);
                if (isLogin) {
                    loginSuccess(response);
                    return;
                }
                loginFail(response);
            } else if (request.getPath().equals("/user/list")) {
                if (!isLogin(request)) {
                    response.sendRedirect("/index.html");
                    return;
                }
                response.forwardBody(createUserListBody().getBytes(UTF_8));
            } else {
                response.forward(request.getPath());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String createUserListBody() {
        List<User> users = new ArrayList<>(DataBase.findAll());
        return createUserTable(users).toString();
    }

    private StringBuilder createUserTable(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>").append("\n");
        sb.append("    <thead>").append("\n");
        sb.append("        <tr> <th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th> </tr>").append("\n");
        sb.append("    </thead>").append("\n");
        sb.append("    <tbody>").append("\n");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            sb.append("<tr>");
            sb.append("<th>").append(i + 1).append("</th>");
            sb.append("<td>").append(user.getUserId()).append("</td>");
            sb.append("<td>").append(user.getName()).append("</td>");
            sb.append("<td>").append(user.getEmail()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("    </tbody>").append("\n");
        sb.append("</table>").append("\n");
        return sb;
    }

    private void loginSuccess(HttpResponse response) {
        response.addHeader("Set-Cookie", "logined=true; Path=/");
        response.sendRedirect("/index.html");
    }

    private void loginFail(HttpResponse response) {
        response.addHeader("Set-Cookie", "logined=false; Path=/");
        response.sendRedirect("/user/login_failed.html");
    }

    private boolean login(HttpRequest request) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User foundUser = DataBase.findUserById(userId);
        if (foundUser == null) {
            return false;
        }
        return foundUser.getPassword().equals(password);
    }

    private User createUser(HttpRequest request) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        return new User(userId, password, name, email);
    }

    private boolean isLogin(HttpRequest request) {
        return request.getCookie("logined").equals("true");
    }
}
