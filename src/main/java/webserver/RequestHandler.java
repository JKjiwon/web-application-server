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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            RequestLine requestLine = extractRequestLine(input);
            Map<String, String> headers = extractHeaders(input);

            if (requestLine.isPost() && requestLine.getPath().startsWith("/user/create")) {
                String requestBody = readRequestBody(headers, input);
                log.debug("Request Body {}", requestBody);
                User user = createUser(requestBody);
                DataBase.addUser(user);
                log.info("User created! {}", user);
                redirectUrl(output, "/index.html");
            } else if (requestLine.isPost() && requestLine.getPath().startsWith("/user/login")) {
                String requestBody = readRequestBody(headers, input);
                log.debug("Request Body {}", requestBody);
                boolean isLogin = login(requestBody);
                if (isLogin) {
                    loginSuccess(output);
                    return;
                }
                loginFail(output);
            } else if (requestLine.getPath().equals("/user/list")) {
                if (!isLogined(headers)) {
                    redirectUrl(output, "/index.html");
                    return;
                }
                writeUserList(output);
            } else if (requestLine.isGet() && requestLine.getPath().endsWith(".css")) {
                writeCss(requestLine.getPath(), output);
            } else {
                writeHtml(Files.readAllBytes(Path.of("./webapp" + requestLine.getPath())), output);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private RequestLine extractRequestLine(BufferedReader input) throws IOException {
        String line = input.readLine();
        if (line == null || line.isEmpty()) {
            throw new IOException("No request line received");
        }
        String path = extractPath(line);
        log.debug("Path {}", path);
        String method = extractMethod(line);
        return new RequestLine(method, path);
    }

    private static Map<String, String> extractHeaders(BufferedReader input) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine = null;
        while (!(headerLine = input.readLine()).isEmpty()) {
            String[] parts = headerLine.split(":");
            headers.put(parts[0].trim(), parts[1].trim());
            log.debug("Header {}", headerLine);
        }
        return headers;
    }

    private void writeCss(String path, DataOutputStream output) throws IOException {
        byte[] body = Files.readAllBytes(Path.of("./webapp" + path));
        response200Header(output, "text/css", body.length);
        responseBody(output, body);
    }

    private void writeUserList(DataOutputStream output) {
        List<User> users = new ArrayList<>(DataBase.findAll());
        StringBuilder sb = makeUserTable(users);
        response200Header(output, "text/html", sb.toString().getBytes(UTF_8).length);
        responseBody(output, sb.toString().getBytes(UTF_8));
    }

    private void writeHtml(byte[] body, DataOutputStream output) throws IOException {
        response200Header(output, "text/html", body.length);
        responseBody(output, body);
    }

    private void redirectUrl(DataOutputStream output, String url) {
        try {
            output.writeBytes("HTTP/1.1 302 Found\r\n");
            output.writeBytes("Location: " + url + "\r\n");
            output.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            output.writeBytes("\r\n");
            output.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private StringBuilder makeUserTable(List<User> users) {
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

    private String extractMethod(String requestLine) {
        String[] parts = requestLine.split(" ");
        return parts[0];
    }

    private void loginSuccess(DataOutputStream output) {
        try {
            output.writeBytes("HTTP/1.1 302 Found\r\n");
            output.writeBytes("Location: /index.html\r\n");
            output.writeBytes("Set-Cookie: logined=true; Path=/\r\n");
            output.writeBytes("\r\n");
            output.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void loginFail(DataOutputStream output) {
        try {
            output.writeBytes("HTTP/1.1 302 Found\r\n");
            output.writeBytes("Location: /user/login_failed.html\r\n");
            output.writeBytes("Set-Cookie: logined=false; Path=/\r\n");
            output.writeBytes("\r\n");
            output.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean login(String requestBody) {
        Map<String, String> userInfos = HttpRequestUtils.parseQueryString(requestBody);
        String userId = URLDecoder.decode(userInfos.getOrDefault("userId", ""), UTF_8);
        String password = URLDecoder.decode(userInfos.getOrDefault("password", ""), UTF_8);

        User foundUser = DataBase.findUserById(userId);
        if (foundUser == null) {
            return false;
        }
        return foundUser.getPassword().equals(password);
    }

    private String extractPath(String requestLine) {
        String[] parts = requestLine.split(" ");
        return parts[1];
    }

    private String readRequestBody(Map<String, String> headers, BufferedReader input) throws IOException {
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        return IOUtils.readData(input, contentLength);
    }

    private User createUser(String requestBody) {
        Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(requestBody);
        String userId = queryStringMap.getOrDefault("userId", "");
        String password = queryStringMap.getOrDefault("password", "");
        String name = queryStringMap.getOrDefault("name", "");
        String email = queryStringMap.getOrDefault("email", "");
        return new User(userId, password, name, email);
    }

    private boolean isLogined(Map<String, String> headers) {
        Map<String, String> cookie = HttpRequestUtils.parseCookies(headers.get("Cookie"));
        return cookie.getOrDefault("logined", "false").equals("true");
    }

    private void response200Header(DataOutputStream output, String contentType, int lengthOfBodyContent) {
        try {
            output.writeBytes("HTTP/1.1 200 OK\r\n");
            output.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
