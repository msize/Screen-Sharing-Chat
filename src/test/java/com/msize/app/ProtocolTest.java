package com.msize.app;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class ProtocolTest {

    private static final int MAX_MESSAGE_LENGTH = 10;
    private static final String
        TYPE = "type",
        HELLO = "hello",
        LIST = "list",
        CHAT = "chat",
        SCREEN = "screen",
        MESSAGE = "message",
        USER = "user",
        SERVER = "server",
        FIRST_USER_NAME = "User 1",
        SECOND_USER_NAME = "User 2",
        CHAT_MESSAGE = "hello world!",
        TIME = "\"time\":\"",
        MSG_LEN = "\"msglen\":" + MAX_MESSAGE_LENGTH,
        TYPE_HELLO = "\"" + TYPE + "\":\"" + HELLO + "\"",
        TYPE_LIST = "\"" + TYPE + "\":\"" + LIST + "\"",
        TYPE_CHAT = "\"" + TYPE + "\":\"" + CHAT + "\"",
        TYPE_SCREEN = "\"" + TYPE + "\":\"" + SCREEN + "\"",
        FROM_USER_SERVER = "\"" + USER + "\":\"" + SERVER + "\"",
        FROM_FIRST_USER = "\"" + USER + "\":\"" + FIRST_USER_NAME + "\"",
        MSG_FIRST_USER_LEFT = "\"" + MESSAGE + "\":\"" + FIRST_USER_NAME + " left the chat\"",
        MSG_FIRST_USER_JOIN = "\"" + MESSAGE + "\":\"" + FIRST_USER_NAME + " joined the chat\"",
        MSG_CHAT_FROM_USER = "\"" + MESSAGE + "\":\"" + CHAT_MESSAGE + "\"";

    @Test
    public void aHello() {
        String hello = Protocol.hello(MAX_MESSAGE_LENGTH);
        assertTrue(hello.contains(TYPE_HELLO));
        assertTrue(hello.contains(MSG_LEN));
    }

    @Test
    public void aUserlist() {
        Collection<String> users = new ArrayList<>();
        users.add(FIRST_USER_NAME);
        users.add(SECOND_USER_NAME);
        String userList = Protocol.userlist(users);
        assertTrue(userList.contains(TYPE_LIST));
        assertTrue(userList.contains(FIRST_USER_NAME));
        assertTrue(userList.contains(SECOND_USER_NAME));
    }

    @Test
    public void aLeftChat() {
        String leftChat = Protocol.leftChat(FIRST_USER_NAME);
        assertTrue(leftChat.contains(TYPE_CHAT));
        assertTrue(leftChat.contains(TIME));
        assertTrue(leftChat.contains(FROM_USER_SERVER));
        assertTrue(leftChat.contains(MSG_FIRST_USER_LEFT));
    }

    @Test
    public void aJoinChat() {
        String joinChat = Protocol.joinChat(FIRST_USER_NAME);
        assertTrue(joinChat.contains(TYPE_CHAT));
        assertTrue(joinChat.contains(TIME));
        assertTrue(joinChat.contains(FROM_USER_SERVER));
        assertTrue(joinChat.contains(MSG_FIRST_USER_JOIN));
    }

    @Test
    public void aScreen() {
        String screen = Protocol.screen();
        assertTrue(screen.contains(TYPE_SCREEN));
    }

    @Test
    public void aChat() {
        String chat = Protocol.chat(FIRST_USER_NAME, CHAT_MESSAGE);
        assertTrue(chat.contains(TYPE_CHAT));
        assertTrue(chat.contains(TIME));
        assertTrue(chat.contains(FROM_FIRST_USER));
        assertTrue(chat.contains(MSG_CHAT_FROM_USER));
    }

}
