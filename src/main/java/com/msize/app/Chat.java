package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;

public interface Chat {

    void newConnection(Session session);

    void closeConnection(Session user, int statusCode, String reason);

    void processMessage(Session user, String message);

    void updateScreen();

}
