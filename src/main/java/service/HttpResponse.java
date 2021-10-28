package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private OutputStream outputStream;
    private DataOutputStream dataOutputStream;

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.dataOutputStream = new DataOutputStream(outputStream);
    }

    public void sendRedirect(String s) {

    }

    public void addHeader(String s, String s1) {

    }

    public void forward(String s) {

    }

    public void forwardBody(String s) {

    }

    public void response200Header(int i) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");
            dataOutputStream.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dataOutputStream.writeBytes("Content-Length: " + i + "\r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] bytes) {
        try {
            if (bytes == null) {
                return;
            }
            dataOutputStream.write(bytes, 0, bytes.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void processHeaders() {

    }
}
