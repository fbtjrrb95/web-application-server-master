package service;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;


public class RequestLineTest {

    @Test
    public void create_method() {

        RequestLine requestLine = new RequestLine("GET /index.html HTTP/1.1");
        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/index.html");

    }

    @Test
    public void create_path_and_params_get() {
        RequestLine requestLine = new RequestLine("GET /user/create?userId=javajigi&password=pass HTTP/1.1");

        assertThat(requestLine.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(requestLine.getPath()).isEqualTo("/user/create");

        Map<String, String> params = requestLine.getParams();
        assertThat(params).hasSize(2)
                .contains(entry("userId", "javajigi"), entry("password", "pass"));


    }

}