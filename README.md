# Test task Screen sharing with chat

The task is to make a screen sharing app with a chat functionality.

It is to be a client-server application, where the Host exposes its screen and
plays as a chat server, and the Client is a web application that shows what the
server exposes and also allows to send/receive short messages.

The exact feature list and UI is up to you to decide, minimal requirements would be:

* multiple simultaneous clients
* still picture of the screen, that is updated periodically
* a single global "chat room" accessible without authentication
* newly connected clients are not required to receive any chat history
* host is to know how many clients are connected

Consider this as a real open source project you'd publish. Think of MVP and
invest in quality rather than additional functionality. A product should be seen
as something that is a pleasure to use.

# Solution

Using technologies:

* Java as base language for server side
* Spark framework and WebSockets for chat
* JSON for structured messages in bidirectional client/server protocol
* JUnit for unit testing and EasyMock for mocking dependent objects
* Maven for assembling all of this in application

# Testing

```
mvn test assembly:single
```

# Build

```
mvn compile assembly:single
```

# Run

```
java -cp target/msize-app-1.0-jar-with-dependencies.jar com.msize.app.Main
```

# Using

Open in browser:
`http://[host address]:4567`

# Stop

Press in console with runned java:
`Ctrl + C`
