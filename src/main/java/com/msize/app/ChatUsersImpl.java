package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatUsersImpl implements  ChatUsers {

    private Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    private int nextUserNumber = 0;

    @Override
    public void add(Session user, String name) {
        if (name.isEmpty())
            name = "Anonymous " + ++nextUserNumber;
        userUsernameMap.put(user, name);
    }

    @Override
    public String extract(Session user) {
        String userName = userUsernameMap.get(user);
        userUsernameMap.remove(user);
        return userName;
    }

    @Override
    public String getName(Session user) {
        return userUsernameMap.get(user);
    }

    @Override
    public Collection<String> getNames() {
        return userUsernameMap.values();
    }

    @Override
    public void broadcastMessage(String message) {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(
                session -> directMessage(session, message));
    }

    @Override
    public void directMessage(Session user, String message) {
        try {
            user.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
