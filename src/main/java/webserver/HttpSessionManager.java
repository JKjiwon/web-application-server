package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpSessionManager {
    private Map<String, HttpSession> sessionMap = new HashMap<>();

    public void setSession(String sessionId, HttpSession session) {
        sessionMap.put(sessionId, session);
    }

    public HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

}
