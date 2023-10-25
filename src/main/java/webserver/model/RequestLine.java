package webserver.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestLine {

    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);

    private HttpMethod method;

    private String path;

    private Map<String, String> parameters = new HashMap<>();

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    Map<String, String> getParameters() {
        return parameters;
    }

    public RequestLine(String requestLine) {
        log.debug("[Request Line] {}", requestLine);

        String[] tokens = requestLine.split(" ");

        if (tokens.length != 3) {
            throw new IllegalArgumentException("Request line[\'" + requestLine + "\'] is illegal format");
        }

        method = HttpMethod.valueOf(tokens[0]);

        if (method.isPost()) {
            path = tokens[1];
            return;
        }
        int index = tokens[1].indexOf("?");

        if (index != -1) {
            path = tokens[1].substring(0, index);
            String queryString = tokens[1].substring(index + 1);
            parameters = HttpRequestUtils.parseQueryString(queryString);
        } else {
            path = tokens[1];
        }
    }
}
