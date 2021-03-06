package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpRequest;
import service.HttpResponse;

import java.io.IOException;

public class CreateUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String userId = httpRequest.getParameter("userId");
        String password = httpRequest.getParameter("password");
        String name = httpRequest.getParameter("name");
        String email = httpRequest.getParameter("email");

        User user = new User(userId, password, name, email);
        log.debug("User : {} ", DataBase.findUserById(userId));

        DataBase.addUser(user);

        httpResponse.sendRedirect("/index.html");
    }
}
