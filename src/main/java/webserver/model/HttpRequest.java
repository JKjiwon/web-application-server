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

    private RequestLine requestLine;

    private Map<String, String> headers;

    private Map<String, String> parameters;

    public static class RequestLine {
        private String httpMethod;
        private String uri;
        private String httpVersion;

        public RequestLine(String httpMethod, String uri, String httpVersion) {
            this.httpMethod = httpMethod;
            this.uri = uri;
            this.httpVersion = httpVersion;
        }

        public String getHttpMethod() {
            return httpMethod;
        }

        public String getUri() {
            return uri;
        }

        public String getHttpVersion() {
            return httpVersion;
        }
    }

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String requestLineRead = br.readLine();
        if (requestLineRead == null) {
            return;
        }
        String[] requestLineTokens = requestLineRead.split(" ");
        requestLine = new RequestLine(requestLineTokens[0], requestLineTokens[1], requestLineTokens[2]);

        headers = new HashMap<>();
        String header;
        while (!(header = br.readLine()).equals("")) {
            log.debug("header: {}", header);
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(header);
            headers.put(pair.getKey(), pair.getValue());
        }

        String uri = requestLine.getUri();
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

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
