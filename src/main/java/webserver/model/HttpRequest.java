package webserver.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine requestLine;

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = br.readLine();
            if (line == null) {
                return;
            }

            requestLine = new RequestLine(line);

            processHeaders(br);

            if (getMethod().isPost()) {
                int contentLength = Integer.parseInt(getHeader("Content-Length"));
                parameters = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
            } else {
                parameters = requestLine.getParameters();
            }

            log.debug("[HttpRequest] {}", this);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getCookie(String key) {
        String cookies = getHeader("Cookie");
        Map<String, String> cookieMap = HttpRequestUtils.parseCookies(cookies);
        return cookieMap.get(key);
    }

    private void processHeaders(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());
        }
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + requestLine.getMethod() +
                ", path='" + requestLine.getPath() + '\'' +
                ", headers=" + headers +
                ", parameters=" + parameters +
                '}';
    }
}
