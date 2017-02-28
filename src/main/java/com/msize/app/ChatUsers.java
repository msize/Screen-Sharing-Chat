package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Collection;

/**
 * A {@code ChatUsers} can provide can provide all the necessary
 * operations for a chat with the user entries.
 */

public interface ChatUsers {

    /**
     * Add new entry in a user registry. Ignore addition, if
     * a entry with the `user` session already in the registry.
     * Generate anonymously user name if `name` is empty.
     *
     * @param user is a WebSocket session with web application.
     * @param name is a user name who have a active session with chat.
     */
    void add(Session user, String name);

    /**
     * Extract entry with the `user` session from registry.
     *
     * @param user is a WebSocket session with web application.
     * @return the name of extracted user.
     */
    String extract(Session user);

    /**
     * Get `name` from entry with the `user` session from registry.
     *
     * @param user is a WebSocket session with web application.
     * @return the name of user who have a active session with chat.
     */
    String getName(Session user);

    /**
     * Get all names from registry.
     *
     * @return user names collection.
     */
    Collection<String> getNames();

    /**
     * Send message to all active `user` sessions.
     *
     * @param message is a structured body of message with
     *                specific attributes serialized as string.
     */
    void broadcastMessage(String message);

    /**
     * Send message to concrete `user` session.
     *
     * @param user is a WebSocket session with web application.
     * @param message is a structured body of message with
     *                specific attributes serialized as string.
     */
    void directMessage(Session user, String message);

}
