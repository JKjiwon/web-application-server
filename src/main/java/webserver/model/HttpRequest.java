package webserver.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String httpMethod;
    private String uri;
    private String httpVersion;

    private Map<String, String> headers;

    private Map<String, String> parameters;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String requestLine = br.readLine();
        log.debug("request Line: {}", requestLine);

        if (requestLine == null) {
            return;
        }
        String[] requestLineTokens = requestLine.split(" ");
        httpMethod = requestLineTokens[0];
        uri = requestLineTokens[1];
        httpVersion = requestLineTokens[2];

        headers = new HashMap<>();
        String header;
        while (!(header = br.readLine()).equals("")) {
            log.debug("header: {}", header);
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(header);
            headers.put(pair.getKey(), pair.getValue());
        }

        if (uri.contains("?")) {
            int idx = uri.indexOf("?");
            String params = uri.substring(idx + 1);
            parameters = HttpRequestUtils.parseQueryString(params);
        }

        if (headers.get("Content-Length") != null) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            String params = IOUtils.readData(br, contentLength);
            parameters = HttpRequestUtils.parseQueryString(params);
        }
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
