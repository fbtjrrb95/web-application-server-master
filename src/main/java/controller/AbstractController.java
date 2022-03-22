package controller;

import service.HttpMethod;
import service.HttpRequest;
import service.HttpResponse;

import java.io.IOException;

public abstract class AbstractController implements Controller {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpMethod method = httpRequest.getMethod();
        if (method.isPost()) {
            doPost(httpRequest, httpResponse);
        } else {
            doGet(httpRequest, httpResponse);
        }
    }

    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    }

    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    }
}
