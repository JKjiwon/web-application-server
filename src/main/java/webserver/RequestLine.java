package webserver;

import util.HttpRequestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestLine {
    private final String method;
    private final String path;
    private final Map<String, String> queryParams;

    public RequestLine(String line) throws IOException {
        if (line == null) {
            throw new IOException("EOF: RequestLine isn't received");
        }
        String[] lineTokens = line.split(" ");
        if (lineTokens.length != 3) {
            throw new IOException("Invalid RequestLine");
        }
        this.method = lineTokens[0];
        String[] urlTokens = lineTokens[1].split("\\?", 2);
        path = urlTokens[0];
        if (urlTokens.length > 1) {
            queryParams = HttpRequestUtils.parseQueryString(urlTokens[1]);
        } else {
            queryParams = new HashMap<>();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParams() {
        return Collections.unmodifiableMap(queryParams);
    }
}
