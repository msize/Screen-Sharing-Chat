package com.msize.app;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

class Protocol {

    private static final String
        TYPE = "type",
        HELLO = "hello",
        LIST = "list",
        USERLIST = "userlist",
        CHAT = "chat",
        USER = "user",
        TIME = "time",
        MESSAGE = "message",
        SERVER = "server",
        LEFTCHAT = "left the chat",
        JOINCHAT = "joined the chat",
        SCREEN = "screen",
        TIMEFORMAT = "HH:mm:ss",
        SPACE = " ";

    static String hello() {
        return string(new JSONObject().put(TYPE, HELLO));
    }

    static String userlist(Collection<String> names) {
        return string(new JSONObject()
                .put(TYPE, LIST)
                .put(USERLIST, names));
    }

    static String leftChat(String name) {
        return chat(SERVER, name + SPACE + LEFTCHAT);
    }

    static String joinChat(String name) {
        return chat(SERVER, name + SPACE + JOINCHAT);
    }

    static String screen() {
        return string(new JSONObject().put(TYPE, SCREEN));
    }

    static String chat(String sender, String chatMessage) {
        return string(new JSONObject()
                .put(TYPE, CHAT)
                .put(USER, sender)
                .put(TIME, new SimpleDateFormat(TIMEFORMAT).format(new Date()))
                .put(MESSAGE, chatMessage));
    }

    private static String string(JSONObject jsonObject) {
        return String.valueOf(jsonObject);
    }

}
