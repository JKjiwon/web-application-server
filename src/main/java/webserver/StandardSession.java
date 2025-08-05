package webserver;

import java.util.HashMap;
import java.util.Map;

public class StandardSession implements HttpSession {

    private final String id;

    private Map<String, Object> attributeMap = new HashMap<>();

    public StandardSession(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributeMap.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }

    @Override
    public void invalidate() {
        attributeMap.clear();
    }
}
