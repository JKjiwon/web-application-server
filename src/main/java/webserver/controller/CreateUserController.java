package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.AbstractController;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.util.Map;

public class CreateUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        log.info("call [{}]", CreateUserController.class.getName());
        Map<String, String> params = httpRequest.getParams();
        User user = new User(params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email"));
        DataBase.addUser(user);
        httpResponse.senRedirect("/index.html");
    }
}
