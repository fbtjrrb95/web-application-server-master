package service;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private int totalBytes = 0;

    private final DataOutputStream dataOutputStream;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream outputStream) {
        dataOutputStream = new DataOutputStream(outputStream);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void forward(String url) throws IOException {

        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

        if (url.endsWith(".css")) {
            headers.put("Content-Type", "text/css");
        } else if (url.endsWith(".js")) {
            headers.put("Content-Type", "application/javascript");
        } else {
            headers.put("Content-Type", "text/html;charset=utf-8");
        }

        headers.put("Content-Length", String.valueOf(body.length));
        response200Header(body.length);
        responseBody(body);

    }

    public void forwardBody(String body) throws IOException {
        byte[] bytes = body.getBytes();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", String.valueOf(bytes.length));
        response200Header(bytes.length);
        responseBody(bytes);
    }

    private void response200Header(int lengthOfBodyContent) throws IOException {

        dataOutputStream.writeBytes("HTTP/1.1 200 OK\r\n");
        processHeaders();
        dataOutputStream.writeBytes("\r\n");

    }

    public void sendRedirect(String redirectUrl) throws IOException {

        dataOutputStream.writeBytes("HTTP/1.1 302 Found \r\n");
        processHeaders();
        dataOutputStream.writeBytes("Location: " + redirectUrl + "\r\n");
        dataOutputStream.writeBytes("\r\n");

    }

    private void responseBody(byte[] body) {
        try {
            if (body == null) {
                return;
            }
            dataOutputStream.write(body, 0, body.length);
            dataOutputStream.writeBytes("\r\n");
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHeaders() throws IOException {
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            dataOutputStream.writeBytes(key + ": " + headers.get(key) + "\r\n");
        }

    }


}
