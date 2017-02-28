package com.msize.app;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ScreenCapture {

    private final String format;
    private final boolean inited;
    private final Robot robot;
    private final Rectangle rectangle;
    private final ByteArrayOutputStream byteArrayOutputStream;

    ScreenCapture(String format) throws AWTException {
        this.format = format;
        this.inited = false;
        this.robot = new Robot();
        this.rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }


    void capture() throws IOException {
        byteArrayOutputStream.reset();
        ImageIO.write(robot.createScreenCapture(rectangle), format, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
    }

    byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }

}
