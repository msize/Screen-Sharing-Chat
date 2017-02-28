package com.msize.app;

import java.util.Timer;
import java.util.TimerTask;

class PeriodicalRepeatableTask extends RepeatableTask {

    private int milliseconds;

    PeriodicalRepeatableTask(Command command, int seconds) {
        super(command);
        final int millisecondsMultiplier = 1000;
        this.milliseconds = seconds * millisecondsMultiplier;
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
