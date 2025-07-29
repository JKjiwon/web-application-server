package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

    private final RequestLineV2 requestLine;
    private final Map<String, String> headers;
    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();

    public HttpRequest(BufferedReader br) throws IOException {
        requestLine = extractRequestLine(br);
        log.debug("RequestLine - method: {}, path: {}", requestLine.getMethod(), requestLine.getPath());
        headers = extractRequestHeader(br);
        extractQueryParam();
        extractFormData(br);
        extractCookie();
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public boolean isGetMethod() {
        return requestLine.getMethod().equals("GET");
    }

    public boolean isPostMethod() {
        return requestLine.getMethod().equals("POST");
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    private RequestLineV2 extractRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        return new RequestLineV2(line);
    }

    private Map<String, String> extractRequestHeader(BufferedReader br) throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headerMap.put(pair.getKey(), pair.getValue());
        }
        return headerMap;
    }

    private void extractFormData(BufferedReader br) throws IOException {
        String contentType = headers.getOrDefault("Content-Type", "");
        int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));

        if (isPostMethod() && APPLICATION_FORM_URLENCODED_VALUE.equals(contentType) && contentLength > 0) {
            String body = IOUtils.readData(br, contentLength);
            Map<String, String> formData = HttpRequestUtils.parseQueryString(body);
            parameters.putAll(formData);
        }
    }

    private void extractQueryParam() {
        if (isGetMethod()) {
            parameters.putAll(requestLine.getQueryParams());
        }
    }

    private void extractCookie() {
        String cookie = getHeader("Cookie");
        cookies.putAll(HttpRequestUtils.parseCookies(cookie));
    }
}
