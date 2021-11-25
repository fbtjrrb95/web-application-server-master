package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpRequest;
import service.HttpResponse;
import util.HttpRequestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class ListUserController extends AbstractGetController {

    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

        // 로그인이 되어 있으면 유저 리스트를 반환
        // 되어있지 않으면 로그인화면 반환
        if (!isLogined(httpRequest)) {
            httpResponse.responseResource("/user/login.html");
            return;
        }

        byte[] body = getUserList().getBytes(StandardCharsets.UTF_8);
        httpResponse.responseResource(body);
    }

    private boolean isLogined(HttpRequest httpRequest) {
        return Boolean.parseBoolean(httpRequest.getCookie("logined"));
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
