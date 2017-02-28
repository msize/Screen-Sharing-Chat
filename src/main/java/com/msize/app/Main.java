package com.msize.app;

import java.io.OutputStream;

import static spark.Spark.init;
import static spark.Spark.get;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

public class Main {

    public static void main(String[] args) {
        try {
            AbstractChat chat = new ScreenSharingChat();
            ScreenCapture screenCapture = new ScreenCapture("png");
            final int screenCaptureUpdateInterval = 5;
            RepeatableTask repeatableTask = new PeriodicalRepeatableTask(() -> {
                screenCapture.capture();
                chat.updateScreen();
            }, screenCaptureUpdateInterval);
            screenCapture.init();
            repeatableTask.run();
            staticFiles.location("/public");
            final int connectionExpirationTime = 600;
            staticFiles.expireTime(connectionExpirationTime);
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
