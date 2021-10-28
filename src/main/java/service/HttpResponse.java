package service;

import java.io.OutputStream;

public class HttpResponse {

    private OutputStream outputStream;

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
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

    }

    public void responseBody(byte[] bytes) {

    }

    public void processHeaders() {

    }
}
