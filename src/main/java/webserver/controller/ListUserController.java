package webserver.controller;

import db.DataBase;
import model.User;
import webserver.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

import java.util.ArrayList;
import java.util.List;

public class ListUserController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if (!isLogin(request)) {
            response.sendRedirect("/index.html");
            return;
        }
        response.forwardBody(createUserListBody());
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }

    private boolean isLogin(HttpRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute("user") != null;
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
}
