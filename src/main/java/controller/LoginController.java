package controller;

import db.DataBase;
import model.User;
import service.HttpRequest;
import service.HttpResponse;
import webserver.HttpSession;

import java.io.IOException;

public class LoginController extends AbstractController {
    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestUserId = httpRequest.getParameter("userId");
        String requestPassword = httpRequest.getParameter("password");

        User user = DataBase.findUserById(requestUserId);

        if (user == null || !requestPassword.equals(user.getPassword())) {
            httpResponse.forward("/user/login_failed.html");
            return;
        }

        HttpSession session = httpRequest.getSession();
        session.setAttribute("user", user);
        httpResponse.sendRedirect("/index.html");
    }
}
