package com.msize.app;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.easymock.EasyMock.*;

public class ScreenSharingChatTest {

    private final Chat chat;
    private final Session first_session, second_session, third_session;
    private final RemoteEndpoint remoteEndpoint;

    public ScreenSharingChatTest() {
        super();
        chat = new ScreenSharingChat(new ChatUsersImpl());
        first_session = mock(Session.class);
        second_session = mock(Session.class);
        third_session = mock(Session.class);
        remoteEndpoint = mock(RemoteEndpoint.class);
    }

    @Before
    public void setUp() {
        expect(first_session.isOpen()).andReturn(false).anyTimes();
        expect(second_session.isOpen()).andReturn(false).anyTimes();
        replay(first_session);
        replay(second_session);
        chat.processMessage(first_session, "{\"type\":\"setname\";\"name\":\"\"}");
        chat.processMessage(second_session, "{\"type\":\"setname\";\"name\":\"\"}");
        reset(first_session);
        reset(second_session);
        reset(third_session);
        reset(remoteEndpoint);
    }

    @Test
    public void aNewConnection() {
        try {
            expect(third_session.getRemote()).andStubReturn(remoteEndpoint);
            remoteEndpoint.sendString("{\"type\":\"hello\"}");
            replay(third_session);
            replay(remoteEndpoint);
            chat.newConnection(third_session);
            verify(third_session);
            verify(remoteEndpoint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void aCloseConnection() {
        try {
            expect(first_session.isOpen()).andReturn(true).anyTimes();
            expect(second_session.isOpen()).andReturn(false).anyTimes();
            expect(first_session.getRemote()).andStubReturn(remoteEndpoint);
            remoteEndpoint.sendString(and(contains("\"type\":\"chat\""), contains("left the chat")));
            remoteEndpoint.sendString(contains("\"type\":\"list\""));
            replay(first_session);
            replay(second_session);
            replay(remoteEndpoint);
            chat.closeConnection(second_session, 0, "");
            verify(first_session);
            verify(second_session);
            verify(remoteEndpoint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void aProcessMessage() {
        try {
            expect(first_session.isOpen()).andReturn(true).anyTimes();
            expect(second_session.isOpen()).andReturn(true).anyTimes();
            expect(first_session.getRemote()).andStubReturn(remoteEndpoint);
            expect(second_session.getRemote()).andStubReturn(remoteEndpoint);
            remoteEndpoint.sendString(and(contains("\"type\":\"chat\""), contains("\"message\":\"Hi!\"")));
            expectLastCall().times(2);
            replay(first_session);
            replay(second_session);
            replay(remoteEndpoint);
            chat.processMessage(first_session, "{\"type\":\"chat\";\"message\":\"Hi!\"}");
            verify(first_session);
            verify(second_session);
            verify(remoteEndpoint);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void aUpdateScreen() {
        try {
            expect(first_session.isOpen()).andReturn(true).anyTimes();
            expect(second_session.isOpen()).andReturn(true).anyTimes();
            expect(first_session.getRemote()).andStubReturn(remoteEndpoint);
            expect(second_session.getRemote()).andStubReturn(remoteEndpoint);
            remoteEndpoint.sendString(contains("\"type\":\"screen\""));
            expectLastCall().times(2);
            replay(first_session);
            replay(second_session);
            replay(remoteEndpoint);
            chat.updateScreen();
            verify(first_session);
            verify(second_session);
            verify(remoteEndpoint);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
