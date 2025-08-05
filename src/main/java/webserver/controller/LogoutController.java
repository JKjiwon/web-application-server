package webserver.controller;

import webserver.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

public class LogoutController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("/");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }
}
