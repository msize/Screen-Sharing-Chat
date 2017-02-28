package com.msize.app;

import java.util.concurrent.Callable;

abstract class RepeatableTask {

    private final Callable<Void> command;

    RepeatableTask(Callable<Void> command) {
        this.command = command;
    }

    void execute() throws Exception {
        command.call();
    }

    abstract void run();

}
