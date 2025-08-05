package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpSessionManager {

    private final Map<String, HttpSession> sessions = new HashMap<>();

    public void setSession(String sessionId, HttpSession session) {
        sessions.put(sessionId, session);
    }

    public HttpSession getSession(String sessionId) {
        HttpSession session = sessions.get(sessionId);
        if (session == null) {
            session = new StandardSession(sessionId, this);
            setSession(sessionId, session);
            return session;
        }
        return session;
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

}
