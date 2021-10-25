package service;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(String.format("%sHttp_GET.txt", testDirectory)));

        HttpRequest httpRequest = new HttpRequest(inputStream);

        assertThat(httpRequest.getMethod()).isEqualTo("GET");
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getParameter("userId")).isEqualTo("javajigi");

    }

    @Test
    public void request_POST() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(String.format("%sHttp_POST.txt", testDirectory)));

        HttpRequest httpRequest = new HttpRequest(inputStream);

        assertThat(httpRequest.getMethod()).isEqualTo("POST");
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
        assertThat(httpRequest.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.getParameter("userId")).isEqualTo("javajigi");

    }


}
