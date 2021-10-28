package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final String protocol = "HTTP/1.1";
    private DataOutputStream dataOutputStream;
    private String statusLine;
    private Map<String, String> responseHeaderMap;

    public HttpResponse(OutputStream outputStream) {
        dataOutputStream = new DataOutputStream(outputStream);
        responseHeaderMap = new HashMap<>();
    }

    public void sendRedirect(String s) {
        responseHeaderMap.put("Location", s);
    }

    public void addHeader(String key, String value) {
        responseHeaderMap.put(key, value);
    }

    public void forward(String s) {

    }

    public void response200Header(int i) {
        statusLine = String.format("%s 200 OK", protocol);
        responseHeaderMap.put("Content-Type", "text/html;charset=utf-8");
        responseHeaderMap.put("Content-Length", String.valueOf(i));
    }

    public void responseBody(byte[] bytes) {

    }

    public void processHeaders() {
        try {
            dataOutputStream.writeBytes(String.format("%s\r\n", statusLine));
            for (Map.Entry<String, String> e : responseHeaderMap.entrySet()) {
                dataOutputStream.writeBytes(String.format("%s: %s\r\n", e.getKey(), e.getValue()));
            }
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
