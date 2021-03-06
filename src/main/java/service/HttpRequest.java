package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.HttpSession;
import webserver.HttpSessions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final Map<String, String> headers = new HashMap<>();
    private Map<String, String> paramsMap = new HashMap<>();
    private RequestLine requestLine;

    public HttpRequest(InputStream inputStream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            requestLine = new RequestLine(line);

            buildHeadersMap(br);
            buildParamsMap(br);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void buildParamsMap(BufferedReader br) throws IOException {

        if (getMethod().isPost()) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            paramsMap = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
        } else {
            paramsMap = requestLine.getParams();
        }
        log.debug("paramsMap : {} ", paramsMap);
    }

    private void buildHeadersMap(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!"".equals(line)) {
            if (line == null) {
                break;
            }
            
            log.debug("header : {}", line);
            String[] tokens = line.split(":");
            headers.put(tokens[0].trim(), tokens[1].trim());

            line = br.readLine();
        }
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return paramsMap.get(key);
    }

    public String getCookie(String key) { return headers.get(key); }

    public HttpSession getSession() {
        return HttpSessions.getSession(getCookie("JSESSIONID"));
    }
}
