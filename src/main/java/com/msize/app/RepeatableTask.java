package com.msize.app;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

class RepeatableTask {

    private static final int MILLISECONDS_MULTIPLIER = 1000;
    private final int milliseconds;
    private final Callable<Void> command;

    RepeatableTask(Callable<Void> command, int seconds) {
        this.milliseconds = seconds * MILLISECONDS_MULTIPLIER;
        this.command = command;
    }

    void run() {
        Timer timer = new Timer();
        timer.schedule(new DeferredTask(), 0, milliseconds);
    }

    private class DeferredTask extends TimerTask {

        @Override
        public void run() {
            try {
                command.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
