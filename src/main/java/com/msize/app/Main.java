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
    private static final String SCREEN_IMAGE_FORMAT = "png";
    private static final String SCREEN_IMAGE_FILE_NAME = "/screen." + SCREEN_IMAGE_FORMAT;
    private static final String STATIC_FILES_LOCATION = "/public";
    private static final String WEB_SOCKET_PATH = "/chat";
    private static final String AWT_EXCEPTION_MESSAGE = "Abstract Window Toolkit can't be initialized.";

    public static void main(String[] args) {
        try {
            Chat chat = new ScreenSharingChat(new ChatUsersImpl());
            ScreenCapture screenCapture = new ScreenCapture(SCREEN_IMAGE_FORMAT);
            RepeatableTask repeatableTask = new RepeatableTask(() -> {
                screenCapture.capture();
                chat.updateScreen();
                return null;
            }, SCREEN_CAPTURE_UPDATE_INTERVAL_SEC);
            repeatableTask.run();
            staticFiles.location(STATIC_FILES_LOCATION);
            staticFiles.expireTime(CONNECTION_EXPIRATION_TIME_SEC);
            webSocket(WEB_SOCKET_PATH, new WebSocketHandler(chat));
            get(SCREEN_IMAGE_FILE_NAME, (req, res) -> {
                OutputStream out = res.raw().getOutputStream();
                out.write(screenCapture.getBytes());
                out.flush();
                out.close();
                return res.raw();
            });
            init();
        } catch (AWTException e) {
            System.err.println(AWT_EXCEPTION_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
