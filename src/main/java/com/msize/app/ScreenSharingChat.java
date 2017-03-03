package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScreenSharingChat implements Chat {

    // A map from `type` of client requests to appropriate `handler`
    private final Map<String, RequestHandler> requestHandlerMap = new HashMap<>();
    private final ChatUsers chatUsers;
    private static final int MAX_MESSAGE_LENGTH = 200;
    private static final String
        TYPE = "type",
        SETNAME = "setname",
        CHAT = "chat",
        NAME = "name",
        MESSAGE = "message",
        REPLACE_LT_FROM = "<",
        REPLACE_LT_TO = "&lt;",
        REPLACE_GT_FROM = ">",
        REPLACE_GT_TO = "&gt;";


    ScreenSharingChat(ChatUsers chatUsers) {
        this.chatUsers = chatUsers;
        initRequestHandlers();
    }

    @Override
    public void newConnection(Session user) {
        chatUsers.directMessage(user, Protocol.hello(MAX_MESSAGE_LENGTH));
    }

    @Override
    public void closeConnection(Session user, int statusCode, String reason) {
        chatUsers.broadcastMessage(Protocol.leftChat(chatUsers.extract(user)));
        chatUsers.broadcastMessage(Protocol.userlist(chatUsers.getNames()));
    }

    @Override
    public void processMessage(Session user, String message) {
        JSONObject request = new JSONObject(stripMessage(message));
        String type = request.getString(TYPE);
        if (requestHandlerMap.containsKey(type))
            requestHandlerMap.get(type).handle(user, request);
    }

    @Override
    public void updateScreen() {
        chatUsers.broadcastMessage(Protocol.screen());
    }

    private void setName(Session user, JSONObject request) {
        chatUsers.add(user, request.getString(NAME));
        chatUsers.broadcastMessage(Protocol.joinChat(chatUsers.getName(user)));
        chatUsers.broadcastMessage(Protocol.userlist(chatUsers.getNames()));
    }

    private void chat(Session user, JSONObject request) {
        String message = request.getString(MESSAGE);
        if (MAX_MESSAGE_LENGTH < message.length())
            message = message.substring(0, MAX_MESSAGE_LENGTH);
        chatUsers.broadcastMessage(Protocol.chat(chatUsers.getName(user), message));
    }

    private String stripMessage(String message) {
        return message.replaceAll(REPLACE_LT_FROM, REPLACE_LT_TO)
                .replaceAll(REPLACE_GT_FROM, REPLACE_GT_TO);
    }

    private void initRequestHandlers() {
        requestHandlerMap.put(SETNAME, this::setName);
        requestHandlerMap.put(CHAT, this::chat);
    }

    private interface RequestHandler {
        void handle(Session user, JSONObject request);
    }

}
