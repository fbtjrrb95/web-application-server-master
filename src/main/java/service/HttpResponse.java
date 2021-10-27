package service;

import java.io.OutputStream;

public class HttpResponse {

    private OutputStream outputStream;
    
    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendRedirect(String s) {
    }
}
