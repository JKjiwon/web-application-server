package controller;

import db.DataBase;
import model.User;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.HttpSession;

public class LoginController extends AbstractController {

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        throw new RuntimeException("Method Not Allowed");
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));

        if (user == null || !user.login(request.getParameter("password"))) {
            // 로그인 실패
            response.sendRedirect("/user/login_failed.html");
            return;
        }
        // 로그인 성공
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect("/index.html");
    }
}
