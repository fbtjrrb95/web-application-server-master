package service;

import org.junit.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {

    private final String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws IOException {
        InputStream inputStream = new FileInputStream(String.format("%sHttp_GET.txt", testDirectory));

        HttpRequest httpRequest = new HttpRequest(inputStream);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getParameter("userId")).isEqualTo("javajigi");

    }

    @Test
    public void request_POST() throws IOException {
        InputStream inputStream = new FileInputStream(String.format("%sHttp_POST.txt", testDirectory));

        HttpRequest httpRequest = new HttpRequest(inputStream);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getParameter("userId")).isEqualTo("javajigi");

    }


}
