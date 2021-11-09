package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpRequest;
import service.HttpResponse;
import util.HttpRequestUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            HttpRequest httpRequest = new HttpRequest(inputStream);
            HttpResponse httpResponse = new HttpResponse(dataOutputStream);

            response(httpRequest, httpResponse);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

        String cookies = httpRequest.getHeader("Cookie");
        String requestPath = httpRequest.getPath();

        Map<String, String> cookiesMap = HttpRequestUtils.parseCookies(cookies);

        log.debug("cookies : {}, requestPath : {}", cookies, requestPath);

        if ("/index.html".equals(requestPath) || "/user/form.html".equals(requestPath) || "/user/login.html".equals(requestPath) || requestPath.endsWith(".css")) {
            httpResponse.responseResource(requestPath);
            return;
        }

        // 로그인 수행
        if ("/user/login".equals(requestPath)) {

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
            return;
        }

        // post 으로 회원 가입
        if ("/user/create".equals(requestPath)) {

            String userId = httpRequest.getParameter("userId");
            String password = httpRequest.getParameter("password");
            String name = httpRequest.getParameter("name");
            String email = httpRequest.getParameter("email");

            User user = new User(userId, password, name, email);
            log.debug("User : {} ", DataBase.findUserById(userId));

            DataBase.addUser(user);

            httpResponse.response302Header("/index.html");
            return;

        }

        // 유저 리스트
        if ("/user/list".equals(requestPath)) {

            boolean isLogined = Boolean.parseBoolean(cookiesMap.get("logined"));

            // 로그인이 되어 있으면 유저 리스트를 반환
            // 되어있지 않으면 로그인화면 반환
            if (!isLogined) {
                httpResponse.responseResource("/user/login.html");
                return;
            }

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

            byte[] body = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
            httpResponse.responseResource(body);
            return;

        }

        byte[] body = "Hello Test World ".getBytes(StandardCharsets.UTF_8);
        httpResponse.responseResource(body);

    }

}
