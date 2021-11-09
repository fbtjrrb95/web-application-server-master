package service;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final String protocol = "HTTP/1.1";
    private final DataOutputStream dataOutputStream;

    private final Map<Integer, String> statusLineMap = ImmutableMap.of(
            200, String.format("%s 200 OK", protocol),
            302, String.format("%s 302 Redirect", protocol)
    );

    public HttpResponse(OutputStream outputStream) {
        dataOutputStream = new DataOutputStream(outputStream);
    }

    public void sendRedirect(String s) {
        try {
            dataOutputStream.writeBytes(String.format("Location: %s\r\n", s));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addHeader(String key, String value) {
        try {
            dataOutputStream.writeBytes(String.format("%s: %s\r\n", key, value));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addStatusLine(int statusCode) {
        try {
            dataOutputStream.writeBytes(statusLineMap.get(statusCode));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseResource(String url) throws IOException {

        if (url.isEmpty()) {
            throw new RuntimeException("url cannot be empty");
        }

        byte[] body = Files.readAllBytes(new File(String.format("./webapp%s", url)).toPath());

        // TODO: refactoring if/else phrase
        if (url.endsWith(".css")) {
            response200CssHeader(body.length);
        } else {
            response200Header(body.length);
        }

        responseBody(body);
    }

    public void responseResource(byte[] bytes) {
        response200Header(bytes.length);
        responseBody(bytes);
    }

    public void responseCssResource(String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200CssHeader(body.length);
        responseBody( body);
    }

    private void response200Header(int lengthOfBodyContent) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");
            dataOutputStream.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dataOutputStream.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200CssHeader(int lengthOfBodyContent) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");
            dataOutputStream.writeBytes("Content-Type: text/css\r\n");
            dataOutputStream.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302Header(String tempUrl) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dataOutputStream.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dataOutputStream.writeBytes(String.format("Location: %s\r\n", tempUrl));
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302LoginSuccessHeader() {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dataOutputStream.writeBytes("Set-Cookie: logined=true \r\n");
            dataOutputStream.writeBytes("Location: /index.html \r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            if (body == null) {
                return;
            }
            dataOutputStream.write(body, 0, body.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }



}
