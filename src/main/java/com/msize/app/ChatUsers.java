package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Collection;

public interface ChatUsers {

    void add(Session user, String name);

    String extract(Session user);

    String getName(Session user);

    Collection<String> getNames();

    void broadcastMessage(String message);

    void directMessage(Session user, String message);

}
