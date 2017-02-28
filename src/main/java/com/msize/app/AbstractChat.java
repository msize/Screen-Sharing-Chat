package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;

abstract class AbstractChat {

    abstract void newConnection(Session session);

    abstract void closeConnection(Session user, int statusCode, String reason);

    abstract void processMessage(Session user, String message);

    abstract void updateScreen();

}
