package service;

import java.io.InputStream;

public class HttpRequest {

    private InputStream inputStream;

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getMethod() {
        return null;
    }

    public String getPath() {
        return null;
    }

    public String getHeader(String connection) {
        return null;
    }

    public String getParameter(String userId) {
        return null;
    }
}
