package com.msize.app;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.easymock.EasyMock.*;

public class ScreenSharingChatTest {

    private final Chat chat;
    private final Session firstSession, secondSession, thirdSession;
    private final RemoteEndpoint remoteEndpoint;

    public ScreenSharingChatTest() {
        super();
        chat = new ScreenSharingChat(new ChatUsersImpl());
        firstSession = mock(Session.class);
        secondSession = mock(Session.class);
        thirdSession = mock(Session.class);
        remoteEndpoint = mock(RemoteEndpoint.class);
    }

    @Before
    public void setUp() {
        expect(firstSession.isOpen()).andReturn(false).anyTimes();
        expect(secondSession.isOpen()).andReturn(false).anyTimes();
        replay(firstSession);
        replay(secondSession);
        chat.processMessage(firstSession, "{\"type\":\"setname\";\"name\":\"\"}");
        chat.processMessage(secondSession, "{\"type\":\"setname\";\"name\":\"\"}");
        reset(firstSession);
        reset(secondSession);
        reset(thirdSession);
        reset(remoteEndpoint);
    }

    @Test
    public void aNewConnection() throws IOException {
        expect(thirdSession.getRemote()).andStubReturn(remoteEndpoint);
        remoteEndpoint.sendString("{\"type\":\"hello\"}");
        replay(thirdSession);
        replay(remoteEndpoint);
        chat.newConnection(thirdSession);
        verify(thirdSession);
        verify(remoteEndpoint);
    }

    @Test
    public void aCloseConnection() throws IOException {
        expect(firstSession.isOpen()).andReturn(true).anyTimes();
        expect(secondSession.isOpen()).andReturn(false).anyTimes();
        expect(firstSession.getRemote()).andStubReturn(remoteEndpoint);
        remoteEndpoint.sendString(and(contains("\"type\":\"chat\""), contains("left the chat")));
        remoteEndpoint.sendString(contains("\"type\":\"list\""));
        replay(firstSession);
        replay(secondSession);
        replay(remoteEndpoint);
        chat.closeConnection(secondSession, 0, "");
        verify(firstSession);
        verify(secondSession);
        verify(remoteEndpoint);
    }

    @Test
    public void aProcessMessage() throws IOException {
        expect(firstSession.isOpen()).andReturn(true).anyTimes();
        expect(secondSession.isOpen()).andReturn(true).anyTimes();
        expect(firstSession.getRemote()).andStubReturn(remoteEndpoint);
        expect(secondSession.getRemote()).andStubReturn(remoteEndpoint);
        remoteEndpoint.sendString(and(contains("\"type\":\"chat\""), contains("\"message\":\"Hi!\"")));
        expectLastCall().times(2);
        replay(firstSession);
        replay(secondSession);
        replay(remoteEndpoint);
        chat.processMessage(firstSession, "{\"type\":\"chat\";\"message\":\"Hi!\"}");
        verify(firstSession);
        verify(secondSession);
        verify(remoteEndpoint);
    }

    @Test
    public void aUpdateScreen() throws IOException {
        expect(firstSession.isOpen()).andReturn(true).anyTimes();
        expect(secondSession.isOpen()).andReturn(true).anyTimes();
        expect(firstSession.getRemote()).andStubReturn(remoteEndpoint);
        expect(secondSession.getRemote()).andStubReturn(remoteEndpoint);
        remoteEndpoint.sendString(contains("\"type\":\"screen\""));
        expectLastCall().times(2);
        replay(firstSession);
        replay(secondSession);
        replay(remoteEndpoint);
        chat.updateScreen();
        verify(firstSession);
        verify(secondSession);
        verify(remoteEndpoint);
    }

}
