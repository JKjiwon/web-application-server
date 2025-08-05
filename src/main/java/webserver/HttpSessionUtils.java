package webserver;

import java.util.UUID;

public class HttpSessionUtils {
    public static final String HTTP_SESSION_ID_KEY = "JSESSIONID";


    public static String getSessionId(HttpRequest request, HttpSessionManager httpSessionManager) {
        String sessionId = request.getCookie(HTTP_SESSION_ID_KEY);
        if (sessionId != null) {
            HttpSession session = httpSessionManager.getSession(sessionId);
            if (session == null) {
                session = new StandardSession(sessionId);
            }
            request.setSession(session);
            return sessionId;
        }

        sessionId = UUID.randomUUID().toString();
        HttpSession session = new StandardSession(sessionId);
        httpSessionManager.setSession(sessionId, session);
        request.setSession(session);
        return sessionId;
    }
}
