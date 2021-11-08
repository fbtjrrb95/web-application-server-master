package controller;

import service.HttpRequest;
import service.HttpResponse;

public abstract class AbstractController implements Controller{
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        if ("GET".equals(httpRequest.getMethod())) {
            doGet(httpRequest, httpResponse);
        }

        if ("POST".equals(httpRequest.getMethod())) {
            doPost(httpRequest, httpResponse);
        }
    }

    public abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse);

    public abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse);
}
