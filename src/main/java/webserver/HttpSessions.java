package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {

    private static Map<String, HttpSession> sessions = new HashMap<>();

    private HttpSessions() {}

    public static HttpSession getSession(String id) {
        HttpSession session = sessions.get(id);

        if (session == null) {
            session = new HttpSession(id);
            sessions.put(id, session);
            return session;
        }

        return sessions.get(id);
    }

    public static void remove(String id) {
        sessions.remove(id);
    }
}
