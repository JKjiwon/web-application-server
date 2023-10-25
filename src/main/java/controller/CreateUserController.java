package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public class CreateUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        throw new RuntimeException("Method Not Allowed");
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        User user = new User(userId, password, name, email);

        DataBase.addUser(user);
        log.debug("user create {}", user);

        response.sendRedirect("/index.html");
    }
}
