package com.msize.app;

import java.io.OutputStream;

import static spark.Spark.init;
import static spark.Spark.get;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

public class Main {

    private static final int SCREEN_CAPTURE_UPDATE_INTERVAL = 5;
    private static final int CONNECTION_EXPIRATION_TIME = 600;

    public static void main(String[] args) {
        try {
            Chat chat = new ScreenSharingChat(new ChatUsersImpl());
            ScreenCapture screenCapture = new ScreenCapture("png");
            RepeatableTask repeatableTask = new PeriodicalRepeatableTask(() -> {
                screenCapture.capture();
                chat.updateScreen();
            }, SCREEN_CAPTURE_UPDATE_INTERVAL);
            repeatableTask.run();
            staticFiles.location("/public");
            staticFiles.expireTime(CONNECTION_EXPIRATION_TIME);
            webSocket("/chat", new WebSocketHandler(chat));
            get("/screen.png", (req, res) -> {
                OutputStream out= res.raw().getOutputStream();
                out.write(screenCapture.getBytes());
                out.flush();
                out.close();
                return res.raw();
            });
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
