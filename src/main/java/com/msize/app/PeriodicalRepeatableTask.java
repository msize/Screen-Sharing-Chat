package com.msize.app;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

class PeriodicalRepeatableTask extends RepeatableTask {

    private static final int MILLISECONDS_MULTIPLIER = 1000;
    private final int milliseconds;

    PeriodicalRepeatableTask(Callable<Void> command, int seconds) {
        super(command);
        this.milliseconds = seconds * MILLISECONDS_MULTIPLIER;
    }

    @Override
    void run() {
        Timer timer = new Timer();
        timer.schedule(new DeferredTask(), 0, milliseconds);
    }

    private class DeferredTask extends TimerTask {

        @Override
        public void run() {
            try {
                execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
