package com.msize.app;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ScreenCapture {

    private String format;
    private boolean inited;
    private Robot robot;
    private Rectangle rectangle;
    private ByteArrayOutputStream byteArrayOutputStream;

    ScreenCapture(String format) {
        this.format = format;
        this.inited = false;
    }

    void init() throws AWTException {
        robot = new Robot();
        rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        byteArrayOutputStream = new ByteArrayOutputStream();
        inited = true;
    }

    void capture() throws ScreenCaptureException, IOException {
        if (!inited)
            throw new ScreenCaptureException("Screen Capture is not inited");
        byteArrayOutputStream.reset();
        ImageIO.write(robot.createScreenCapture(rectangle), format, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
    }

    byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }

}
