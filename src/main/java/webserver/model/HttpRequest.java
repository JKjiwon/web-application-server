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

    private Map<String, String> headers = new HashMap<>();

    private Map<String, String> params = new HashMap<>();


    public HttpRequest(InputStream in) throws IOException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line = br.readLine();
            if (line == null) {
                return;
            }

            requestLine = new RequestLine(line);

            while (!(line = br.readLine()).equals("")) {
                log.debug("header: {}", line);
                HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                headers.put(pair.getKey(), pair.getValue());
            }

            if (requestLine.getMethod().equals(HttpMethod.POST)) {
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                params = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
            } else {
                params = requestLine.getParams();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
