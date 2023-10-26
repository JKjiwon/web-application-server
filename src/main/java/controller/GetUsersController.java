package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.HttpSession;

import java.util.ArrayList;
import java.util.List;

public class GetUsersController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(GetUsersController.class);

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        if (!isLogined(request.getSession())) {
            response.sendRedirect("/index.html");
            return;
        }
        List<User> users = new ArrayList<>(DataBase.findAll());
        byte[] body = responseUsers(users);
        response.forward(body);
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        throw new RuntimeException("Method Not Allowed");
    }

    private boolean isLogined(HttpSession session) {
        return session.getAttribute("user") != null;
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
}
