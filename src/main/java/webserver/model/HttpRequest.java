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

    private HttpMethod method;
    private String path;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line = br.readLine();
        if (line == null) {
            return;
        }

        processRequestLine(line);
        processHeaders(br);

        if (method.equals(HttpMethod.POST)) {
            int contentLength = Integer.parseInt(getHeader("Content-Length"));
            parameters.putAll(HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength)));
        }

        log.debug("[HttpRequest] {}", this);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String headerKey) {
        return headers.get(headerKey);
    }

    public String getParameter(String paramKey) {
        return parameters.get(paramKey);
    }

    private void processHeaders(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());
        }
    }

    private void processRequestLine(String requestLine) {
        String[] requestLineToken = requestLine.split(" ");
        method = HttpMethod.valueOf(requestLineToken[0]);
        if (requestLineToken[1].contains("?")) {
            int index = requestLineToken[1].indexOf("?");
            path = requestLineToken[1].substring(0, index);
            String queryString = requestLineToken[1].substring(index + 1);
            parameters = HttpRequestUtils.parseQueryString(queryString);
        } else {
            path = requestLineToken[1];
        }
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                ", parameters=" + parameters +
                '}';
    }
}
