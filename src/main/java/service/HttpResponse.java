package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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

    public void forward(String s) {
        try {
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            if (bytes == null) {
                return;
            }
            dataOutputStream.write(bytes, 0, bytes.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
