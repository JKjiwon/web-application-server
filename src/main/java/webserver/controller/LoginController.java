package webserver.controller;

import db.DataBase;
import model.User;
import webserver.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

import java.util.Optional;

public class LoginController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Optional<User> optionalUser = login(request);
        if (optionalUser.isPresent()) {
            loginSuccess(request, response, optionalUser.get());
            return;
        }
        loginFail(response);
    }


    private Optional<User> login(HttpRequest request) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User foundUser = DataBase.findUserById(userId);
        if (foundUser == null) {
            return Optional.empty();
        }
        if (foundUser.getPassword().equals(password)) {
            return Optional.of(foundUser);
        }
        return Optional.empty();
    }

    private void loginSuccess(HttpRequest request, HttpResponse response, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect("/index.html");
    }

    private void loginFail(HttpResponse response) {
        response.sendRedirect("/user/login_failed.html");
    }
}
