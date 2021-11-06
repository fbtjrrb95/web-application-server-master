package controller;

import service.HttpRequest;
import service.HttpResponse;

public interface Controller {

    void service(HttpRequest httpRequest, HttpResponse httpResponse);

}
