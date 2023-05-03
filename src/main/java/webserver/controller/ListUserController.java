package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.AbstractController;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.util.Collection;

public class ListUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        log.info("call [{}]", ListUserController.class.getName());
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
    }

    private boolean isLogin(String cookieValue) {
        if (cookieValue == null) {
            return false;
        }
        return Boolean.parseBoolean(cookieValue);
    }
}
