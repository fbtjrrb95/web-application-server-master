package service;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RequestLineTest {

    @Test
    public void create_method() {

        RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1");
        assertThat(requestLine.getMethod()).isEqualTo("GET");
        assertThat(requestLine.getPath()).isEqualTo("/index.html");

    }

}