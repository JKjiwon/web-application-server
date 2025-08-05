package webserver;

import java.util.HashMap;
import java.util.Map;

public class StandardSession implements HttpSession {

    private final String id;

    private final Map<String, Object> attributes = new HashMap<>();

    private final HttpSessionManager sessionManager;

    public StandardSession(String id, HttpSessionManager sessionManager) {
        this.id = id;
        this.sessionManager = sessionManager;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void invalidate() {
        sessionManager.removeSession(id);
    }
}
