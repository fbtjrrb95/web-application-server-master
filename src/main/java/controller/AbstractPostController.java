package controller;

import service.HttpRequest;
import service.HttpResponse;

import java.io.IOException;

public abstract class AbstractPostController implements Controller{
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        doPost(httpRequest, httpResponse);
    }

    public abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
