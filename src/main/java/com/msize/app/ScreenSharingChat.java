package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScreenSharingChat extends AbstractChat {

    private Map<String, RequestHandler> requestHandlerMap = new ConcurrentHashMap<>();
    private Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    private int nextUserNumber = 0;

    ScreenSharingChat() {
        initRequestHandlers();
    }

    @Override
    public void newConnection(Session user) {
        directMessage(user, new JSONObject().put("type", "hello"));
    }

    @Override
    public void closeConnection(Session user, int statusCode, String reason) {
        String userName = userUsernameMap.get(user);
        userUsernameMap.remove(user);
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
        String userName = request.getString("name");
        if (userName.isEmpty())
            userName = "Anonymous " + ++nextUserNumber;
        userUsernameMap.put(user, userName);
        message("Server",userName + " joined the chat");
        userlist();
    }

    private void chat(Session user, JSONObject request) {
        message(userUsernameMap.get(user), request.getString("message"));
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
                .put("userlist", userUsernameMap.values())
        );
    }

    private void broadcastMessage(JSONObject jsonObject) {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(
                session -> directMessage(session, jsonObject));
    }

    private void directMessage(Session user, JSONObject jsonObject) {
        try {
            user.getRemote().sendString(String.valueOf(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRequestHandlers() {
        requestHandlerMap.put("setname", this::setName);
        requestHandlerMap.put("chat", this::chat);
    }

    private interface RequestHandler {
        void handle(Session user, JSONObject request);
    }

}
