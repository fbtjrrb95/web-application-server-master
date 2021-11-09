package controller;

import service.HttpRequest;
import service.HttpResponse;

import java.io.IOException;

public abstract class AbstractGetController implements Controller{
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        doGet(httpRequest, httpResponse);
    }

    public abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
