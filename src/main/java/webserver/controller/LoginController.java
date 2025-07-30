package webserver.controller;

import db.DataBase;
import model.User;
import webserver.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        if (login(request)) {
            loginSuccess(response);
            return;
        }
        loginFail(response);
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

    private void loginSuccess(HttpResponse response) {
        response.addHeader("Set-Cookie", "logined=true; Path=/");
        response.sendRedirect("/index.html");
    }

    private void loginFail(HttpResponse response) {
        response.addHeader("Set-Cookie", "logined=false; Path=/");
        response.sendRedirect("/user/login_failed.html");
    }
}
