package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private Map<String, String> headerMap;
    private Map<String, String> queryParametersMap;
    private Map<String, String> bodyParametersMap;

    private String method;
    private String url;
    private String requestPath;

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line = br.readLine();

        String[] tokens = line.split(" ");
        method = tokens[0];
        url = tokens[1];

        int index = url.indexOf("?");
        if (index > -1) {
            requestPath = url.substring(0, index);
            queryParametersMap = HttpRequestUtils.parseQueryString(url.substring(index + 1));
        } else {
            requestPath = url;
        }

        headerMap = getHeaderMap(br);

        int contentLength = 0;
        String valueOfContentLength = headerMap.get("Content-Length");
        if (valueOfContentLength != null && !valueOfContentLength.isEmpty()) {
            contentLength = Integer.parseInt(valueOfContentLength);
        }

        bodyParametersMap = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));

    }

    private Map<String, String> getHeaderMap(BufferedReader br) throws IOException {
        Map<String, String> _headerMap = new HashMap<>();
        String line = null;
        while (!"".equals(line)) {
            line = br.readLine();
            if (line == null) {
                break;
            }
            log.debug("header : {}", line);

            int _index = line.indexOf(":");
            if (_index > -1) {
                String key = line.substring(0, _index).trim();
                String value = line.substring(_index + 1).trim();
                _headerMap.put(key, value);
            }
        }
        return _headerMap;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return requestPath;
    }

    public String getHeader(String key) {
        return headerMap.get(key);
    }

    public String getParameter(String key) {
        if (method.equals("GET")) {
            return queryParametersMap.get(key);
        }

        if (method.equals("POST")) {
            return bodyParametersMap.get(key);
        }

        return "";
    }
}
