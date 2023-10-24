package controller;

import db.DataBase;
import model.User;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class GetUsersController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if (!isLogined(request)) {
            response.sendRedirect("/index.html");
            return;
        }
        List<User> users = new ArrayList<>(DataBase.findAll());
        byte[] body = responseUsers(users);
        response.forward(body);
    }

    private boolean isLogined(HttpRequest request) {
        String logined = request.getCookie("logined");

        if (logined == null) {
            return false;
        }

        return Boolean.parseBoolean(logined);
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
