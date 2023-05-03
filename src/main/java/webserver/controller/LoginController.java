package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.AbstractController;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

import java.util.Map;

public class LoginController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        log.info("call [{}]", LoginController.class.getName());
        Map<String, String> params = httpRequest.getParams();
        User user = DataBase.findUserById(params.get("userId"));
        if (user == null) {
            httpResponse.addHeader("Set-Cookie", "logined=false");
            httpResponse.senRedirect("/user/login_failed.html");
            return;
        }
        if (user.getPassword() != null && user.getPassword().equals(params.get("password"))) {
            httpResponse.addHeader("Set-Cookie", "logined=true");
            httpResponse.senRedirect("/index.html");
        } else {
            httpResponse.addHeader("Set-Cookie", "logined=false");
            httpResponse.senRedirect("/user/login_failed.html");
        }
    }
}
