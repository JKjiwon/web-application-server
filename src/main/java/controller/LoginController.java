package controller;

import db.DataBase;
import model.User;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public class LoginController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = DataBase.findUserById(userId);

        if (user == null || !user.getPassword().equals(password)) {
            // 로그인 실패
            response.setCookie("logined", "false");
            response.sendRedirect("/user/login_failed.html");
            return;
        }
        // 로그인 성공
        response.setCookie("logined", "true");
        response.sendRedirect("/index.html");
    }
}
