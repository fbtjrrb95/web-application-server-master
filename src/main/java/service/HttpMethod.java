package service;

public enum HttpMethod {
    GET,
    POST;

    public boolean isPost() {
        return this == POST;
    }
}
