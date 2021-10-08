package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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


            String line = br.readLine();

            byte[] body = toBytes(line);

            while (!"".equals(line)) {

                // line null check 를 하지 않으면 무한 루프에 빠질 수 있음.
                if (line == null) {
                    return;
                }

                log.debug(line);
                line = br.readLine();

            }

            response200Header(dos, body.length);
            responseBody(dos, body);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] toBytes(String line) throws IOException {

        String[] tokens = line.split(" ");
        String url = tokens[1];

        int index = url.indexOf("?");
        String requestPath = index > -1 ? url.substring(0, index) : url;
        String params = url.substring(index + 1);
        Map<String, String> queryStringMap = HttpRequestUtils.parseQueryString(params);


        if ("/index.html".equals(requestPath) || "/user/form.html".equals(requestPath)) {
            return Files.readAllBytes(new File("./webapp" + url).toPath());
        }

        if ("/user/create".equals(requestPath)) {

            String userId = queryStringMap.get("userId");
            String password = queryStringMap.get("password");
            String name = queryStringMap.get("name");
            String email = queryStringMap.get("email");

            User user = new User(userId, password, name, email);

            log.debug(user.toString());

            return Files.readAllBytes(new File("./webapp/index.html").toPath());
        }

        String defaultText = "Hello Test World ";
        return defaultText.getBytes();
    }
}
