package service;

import org.junit.Test;

import java.io.*;


// TODO: test code assert 자동화
public class HttpResponseTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void responseRedirect() throws IOException {
        // Http_Redirect.txt 결과는 응답 header 에
        // Location 정보가 /index.html 로 포함되어 있어야 한다.
        HttpResponse httpResponse = new HttpResponse(createOutputStream("Http_Redirect.txt"));
        httpResponse.sendRedirect("/index.html");
    }

    @Test
    public void responseCookies() throws IOException {
        // Http_Cookie.txt 결과는 응답 header 에 Set-Cookie 값으로
        // logined=true 값이 포함되어 있어야 한다.
        HttpResponse httpResponse = new HttpResponse(createOutputStream("Http_Cookie.txt"));
        httpResponse.addHeader("Set-Cookie", "logined=true");
        httpResponse.sendRedirect("/index.html");

    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(String.format("%s%s", testDirectory, filename));
    }

}
