package webserver.core;

import java.util.UUID;

public class HttpSessionUtils {
    public static final String HTTP_SESSION_ID_KEY = "JSESSIONID";


    public static String getSessionId(HttpRequest request) {
        String sessionId = request.getCookie(HTTP_SESSION_ID_KEY);
        if (sessionId == null) {
            return UUID.randomUUID().toString();
        }
        return sessionId;
    }
}
