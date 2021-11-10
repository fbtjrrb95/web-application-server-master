package controller;

import db.DataBase;
import model.User;
import service.HttpRequest;
import service.HttpResponse;

import java.io.IOException;

public class LoginController  extends AbstractPostController {
    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestUserId = httpRequest.getParameter("userId");
        String requestPassword = httpRequest.getParameter("password");

        User userById = DataBase.findUserById(requestUserId);

        // login 실패 시, redirectUrl 설정
        if (userById == null || !requestPassword.equals(userById.getPassword())) {
            httpResponse.responseResource("/user/login_failed.html");
            return;
        }

        // login 성공, redirectUrl 설정
        httpResponse.response302LoginSuccessHeader();
    }
}
