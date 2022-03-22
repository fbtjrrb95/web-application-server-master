package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    private Map<String, Object> values = new HashMap<>();
    private String id;

    public HttpSession(String id) {
        this.id = id;
    }

    public void setAttribute(String name, Object value) {
        this.values.put(name, value);
    }

    public Object getAttribute(String name) {
        return this.values.get(name);
    }

    public void removeAttribute(String name) {
        this.values.remove(name);
    }
}
