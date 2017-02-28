package com.msize.app;

import org.eclipse.jetty.websocket.api.Session;
import org.junit.Test;

import static org.easymock.EasyMock.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatUsersTest {

    private final ChatUsers chatUsers = new ChatUsersImpl();
    private final Session firstSession = mock(Session.class);
    private final Session secondSession = mock(Session.class);
    private static final String
        FIRST_USER_NAME = "",
        SECOND_USER_NAME = "user",
        ANONYMOUS_1 = "Anonymous 1";

    @Test
    public void aAdd() {
        chatUsers.add(firstSession, FIRST_USER_NAME);
        chatUsers.add(firstSession, SECOND_USER_NAME);
        chatUsers.add(secondSession, SECOND_USER_NAME);
        String users = chatUsers.getNames().toString();
        assertTrue(users.contains(ANONYMOUS_1));
        assertTrue(users.contains(SECOND_USER_NAME));
    }

    @Test
    public void aExtract() {
        chatUsers.add(firstSession, FIRST_USER_NAME);
        chatUsers.add(secondSession, SECOND_USER_NAME);
        String userName = chatUsers.extract(secondSession);
        String users = chatUsers.getNames().toString();
        assertEquals(SECOND_USER_NAME, userName);
        assertEquals("[" + ANONYMOUS_1 + "]", users);
    }

    @Test
    public void aGetName() {
        chatUsers.add(firstSession, FIRST_USER_NAME);
        chatUsers.add(secondSession, SECOND_USER_NAME);
        assertEquals(ANONYMOUS_1, chatUsers.getName(firstSession));
        assertEquals(SECOND_USER_NAME, chatUsers.getName(secondSession));
    }

}
