package controller;

import service.HttpRequest;
import service.HttpResponse;

public abstract class AbstractPostController implements Controller{
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        doPost(httpRequest, httpResponse);
    }

    public abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse);
}
