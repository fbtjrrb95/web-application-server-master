package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpRequest;
import service.HttpResponse;
import webserver.HttpSession;

import java.io.IOException;
import java.util.Collection;

public class ListUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

        if (!isLogined(httpRequest.getSession())) {
            httpResponse.sendRedirect("/user/login.html");
            return;
        }

        httpResponse.forwardBody(getUserList());
    }

    private boolean isLogined(HttpSession session) {
        if (session == null) return false;
        Object user = session.getAttribute("user");
        return user != null;
    }

    private String getUserList() {
        Collection<User> users = DataBase.findAll();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<table border='1'>");
        stringBuilder.append("<tr>");
        stringBuilder.append("<td>userId</td>");
        stringBuilder.append("<td>userName</td>");
        stringBuilder.append("<td>userEmail</td>");
        stringBuilder.append("</tr>");

        users.forEach(user -> {
            stringBuilder.append("<tr>");
            stringBuilder.append(String.format("<td>%s</td>", user.getUserId()));
            stringBuilder.append(String.format("<td>%s</td>", user.getName()));
            stringBuilder.append(String.format("<td>%s</td>", user.getEmail()));
            stringBuilder.append("</tr>");
        });
        stringBuilder.append("</table>");

        return stringBuilder.toString();
    }
}
