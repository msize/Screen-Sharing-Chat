package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScreenSharingChat implements Chat {

    private final Map<String, RequestHandler> requestHandlerMap = new HashMap<>();
    private final ChatUsers chatUsers;

    ScreenSharingChat(ChatUsers chatUsers) {
        this.chatUsers = chatUsers;
        initRequestHandlers();
    }

    @Override
    public void newConnection(Session user) {
        directMessage(user, new JSONObject().put("type", "hello"));
    }

    @Override
    public void closeConnection(Session user, int statusCode, String reason) {
        String userName = chatUsers.extract(user);
        message("Server", userName + " left the chat");
        userlist();
    }

    @Override
    public void processMessage(Session user, String message) {
        JSONObject request = new JSONObject(message);
        String type = request.getString("type");
        RequestHandler handler = requestHandlerMap.get(type);
        if (null != handler)
            handler.handle(user, request);
    }

    @Override
    public void updateScreen() {
        broadcastMessage(new JSONObject()
                .put("type", "screen")
        );
    }

    private void setName(Session user, JSONObject request) {
        chatUsers.add(user, request.getString("name"));
        message("Server",chatUsers.getName(user) + " joined the chat");
        userlist();
    }

    private void chat(Session user, JSONObject request) {
        message(chatUsers.getName(user), request.getString("message"));
    }

    private void message(String sender, String message) {
        broadcastMessage(new JSONObject()
                .put("type", "chat")
                .put("user", sender)
                .put("time", new SimpleDateFormat("HH:mm:ss").format(new Date()))
                .put("message", message)
        );
    }

    private void userlist() {
        broadcastMessage(new JSONObject()
                .put("type", "list")
                .put("userlist", chatUsers.getNames())
        );
    }

    private void broadcastMessage(JSONObject jsonObject) {
        chatUsers.broadcastMessage(String.valueOf(jsonObject));
    }

    private void directMessage(Session user, JSONObject jsonObject) {
        chatUsers.directMessage(user, String.valueOf(jsonObject));
    }

    private void initRequestHandlers() {
        requestHandlerMap.put("setname", this::setName);
        requestHandlerMap.put("chat", this::chat);
    }

    private interface RequestHandler {
        void handle(Session user, JSONObject request);
    }

}
