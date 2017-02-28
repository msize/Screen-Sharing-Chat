package com.msize.app;

import java.awt.*;
import java.io.OutputStream;

import static spark.Spark.init;
import static spark.Spark.get;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

public class Main {

    private static final int SCREEN_CAPTURE_UPDATE_INTERVAL_SEC = 5;
    private static final int CONNECTION_EXPIRATION_TIME_SEC = 600;

    public static void main(String[] args) {
        try {
            Chat chat = new ScreenSharingChat(new ChatUsersImpl());
            ScreenCapture screenCapture = new ScreenCapture("png");
            RepeatableTask repeatableTask = new RepeatableTask(() -> {
                screenCapture.capture();
                chat.updateScreen();
                return null;
            }, SCREEN_CAPTURE_UPDATE_INTERVAL_SEC);
            repeatableTask.run();
            staticFiles.location("/public");
            staticFiles.expireTime(CONNECTION_EXPIRATION_TIME_SEC);
            webSocket("/chat", new WebSocketHandler(chat));
            get("/screen.png", (req, res) -> {
                OutputStream out = res.raw().getOutputStream();
                out.write(screenCapture.getBytes());
                out.flush();
                out.close();
                return res.raw();
            });
            init();
        } catch (AWTException e) {
            System.err.println("Abstract Window Toolkit can't be initialized.");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
