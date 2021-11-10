package controller;

import service.HttpRequest;
import service.HttpResponse;

import java.io.IOException;

public class SimpleGetController extends AbstractGetController {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestPath = httpRequest.getPath();
        if (requestPath.isEmpty()) {
            throw new IllegalArgumentException("requestPath cannot be empty");
        }
        httpResponse.responseResource(requestPath);
    }
}
