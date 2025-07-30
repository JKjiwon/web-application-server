package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class CreateUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        log.info("User created! {}", user);
        response.sendRedirect("/index.html");
    }
}
