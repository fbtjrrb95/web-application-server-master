package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
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

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            Map<String, String> headerMap = this.getHeaderMap(br);

            String contentLengthString = headerMap.get("Content-Length");
            int contentLength = 0;
            if (contentLengthString != null && !contentLengthString.isEmpty()) {
                contentLength = Integer.parseInt(contentLengthString);
            }

            Map<String, String> bodyMap = this.getBodyMap(br, contentLength);
            log.debug("headerMap : " + headerMap);
            log.debug("bodyMap : " + bodyMap);

            response(dos, headerMap, bodyMap);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response(DataOutputStream dos, Map<String, String> headerMap, Map<String, String> bodyMap) throws IOException {

        String url = headerMap.get("url");
        String method = headerMap.get("method");
        String params = headerMap.get("params");
        String cookies = headerMap.get("Cookie");

        String requestPath = params != null ? headerMap.get("requestPath") : url;
        Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(params);
        Map<String, String> cookiesMap = HttpRequestUtils.parseCookies(cookies);

        if ("/index.html".equals(requestPath) || "/user/form.html".equals(requestPath)) {
            byte[] body = Files.readAllBytes(new File("./webapp" + requestPath).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
            return;
        }

        // 로그인 뷰 리턴
        if ("/user/login.html".equals(requestPath) && "get".equalsIgnoreCase(method)) {
            byte[] body = Files.readAllBytes(new File("./webapp/user/login.html").toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
            return;
        }

        // 로그인 실패 뷰 리턴
        if ("/user/login_failed.html".equals(requestPath) && "get".equalsIgnoreCase(method)) {
            byte[] body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
            return;
        }

        // 로그인 수행
        if ("/user/login".equals(requestPath) && "post".equalsIgnoreCase(method)) {

            String requestUserId = bodyMap.get("userId");
            String requestPassword = bodyMap.get("password");

            User userById = DataBase.findUserById(requestUserId);

            Map<String, String> hashMap = new HashMap<>();

            // login 실패 시, redirectUrl 설정
            if (userById == null || !requestPassword.equals(userById.getPassword())) {

                String redirectUrl = "/user/login_failed.html";
                String setCookie = "logined=false";

                hashMap.put("Location", redirectUrl);
                hashMap.put("Set-Cookie", setCookie);

                response302Header(dos, hashMap);
                responseBody(dos, null);
                return;
            }

            // login 성공, redirectUrl 설정
            String redirectUrl = "/index.html";
            String setCookie = "logined=true";

            hashMap.put("Location", redirectUrl);
            hashMap.put("Set-Cookie", setCookie);

            response302Header(dos, hashMap);
            responseBody(dos, null);
            return;
        }

        // 회원 가입
        if ("/user/create".equals(requestPath)) {

            String userId = null;
            String password = null;
            String name = null;
            String email = null;

            if ("get".equalsIgnoreCase(method)) {
                if (queryStringMap != null) {
                    userId = queryStringMap.get("userId");
                    password = queryStringMap.get("password");
                    name = queryStringMap.get("name");
                    email = queryStringMap.get("email");
                    User user = new User(userId, password, name, email);
                    DataBase.addUser(user);
                    log.debug("Will Create User Info : " + user);
                    log.debug("Created User Info : " + DataBase.findUserById(userId));

                }
            } else if ("post".equalsIgnoreCase(method)) {
                if (bodyMap != null) {
                    userId = bodyMap.get("userId");
                    password = bodyMap.get("password");
                    name = bodyMap.get("name");
                    email = bodyMap.get("email");
                    User user = new User(userId, password, name, email);
                    DataBase.addUser(user);
                    log.debug("Will Create User Info : " + user);
                    log.debug("Created User Info : " + DataBase.findUserById(userId));
                }
            }

            String tempUrl = "/index.html";
            response302Header(dos, tempUrl);
            responseBody(dos, null);
            return;

        }

        // 유저 리스트
        if ("/user/list".equals(requestPath)) {

            boolean isLogined = Boolean.parseBoolean(cookiesMap.get("logined"));

            // 로그인이 되어 있으면 유저 리스트를 반환
            // 되어있지 않으면 초기화면 반환
            String pathName = "./webapp/user/list.html";
            if (!isLogined) {
                pathName = "./webapp/index.html";
            }

            byte[] body = Files.readAllBytes(new File(pathName).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
            return;

        }

        byte[] defaultBody = "Hello Test World ".getBytes(StandardCharsets.UTF_8);
        response200Header(dos, defaultBody.length);
        responseBody(dos, defaultBody);

    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String tempUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes(String.format("Location: %s\r\n", tempUrl));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    private void response302Header(DataOutputStream dos, Map<String,String> hashMap) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            for (String key : hashMap.keySet()) {
                dos.writeBytes(String.format("%s: %s\r\n", key, hashMap.get(key)));
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            if (body == null) {
                return;
            }
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, String> getHeaderMap(BufferedReader br) throws IOException {

        Map<String, String> headerMap = new HashMap<>();

        String line = br.readLine();
        log.debug(line);

        String[] tokens = line.split(" ");

        String method = tokens[0];
        String url = tokens[1];
        String protocol = tokens[2];

        headerMap.put("method", method);
        headerMap.put("url", url);
        headerMap.put("protocol", protocol);

        // parsing url -> requestPath, queryString
        int index = url.indexOf("?");
        if (index > -1) {
            String requestPath = url.substring(0, index);
            String params = url.substring(index + 1);
            headerMap.put("requestPath", requestPath);
            headerMap.put("params", params);
        }

        while (!"".equals(line)) {
            // line null check 를 하지 않으면 무한 루프에 빠질 수 있음.
            if (line == null) {
                break;
            }
            line = br.readLine();
            log.debug(line);

            int _index = line.indexOf(":");
            if (_index > -1) {
                String key = line.substring(0, _index).trim();
                String value = line.substring(_index + 1).trim();
                headerMap.put(key, value);
            }
        }
        return headerMap;
    }

    private Map<String, String> getBodyMap(BufferedReader br, int contentLength) throws IOException {

        String data = IOUtils.readData(br, contentLength);
        log.debug("data : " + data);
        return HttpRequestUtils.parseQueryString(data);
    }

}
