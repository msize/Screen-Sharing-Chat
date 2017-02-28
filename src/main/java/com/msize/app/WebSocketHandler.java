package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WebSocketHandler {

    private final Chat chat;

    WebSocketHandler(Chat chat) {
        this.chat = chat;
    }

    @OnWebSocketConnect
    public void onConnect(Session user) {
        chat.newConnection(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        chat.closeConnection(user, statusCode, reason);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        chat.processMessage(user, message);
    }

}
