package webserver.controller;

import webserver.core.AbstractController;
import webserver.core.HttpRequest;
import webserver.core.HttpResponse;
import webserver.core.HttpSession;

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
