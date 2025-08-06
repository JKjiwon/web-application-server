package webserver.core;

import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {

    private final Map<String, String> cookies = new HashMap<>();

    public HttpCookie(String cookieValue) {
        cookies.putAll(HttpRequestUtils.parseCookies(cookieValue));
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }
}
