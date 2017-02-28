package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;

/**
 * A {@code Chat} can provide conversation with multiple
 * simultaneous clients in global chat-room without
 * authentication.
 */

public interface Chat {

    /**
     * Processing new connection.
     *
     * After creation connection chat-server sends to client special
     * `hello` message.
     *
     * @param user is a WebSocket session with web application.
     */
    void newConnection(Session user);

    /**
     * Processing closed connection.
     *
     * After close connection chat-server remove entry with them.
     * Send broadcast message with information about left user and
     * send new user list to all active clients.
     *
     * @param user is a WebSocket session with web application.
     * @param statusCode is a WebSocket close code number.
     * @param reason is a WebSocket closing reason description.
     */
    void closeConnection(Session user, int statusCode, String reason);

    /**
     * Processing user message.
     *
     * Processing custom message, determine the type of request and
     * choice of the related certain type of handler.
     *
     * @param user is a WebSocket session with web application.
     * @param message is structured body of user message with
     *                specific attributes serialized as string.
     */
    void processMessage(Session user, String message);

    /**
     * Processing update captured screen.
     *
     * After a new screenshot image is ready send special broadcast
     * `screen` message to all active clients.
     */
    void updateScreen();

}
