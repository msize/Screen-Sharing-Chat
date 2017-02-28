package com.msize.app;

abstract class RepeatableTask {

    private final Command command;

    RepeatableTask(Command command) {
        this.command = command;
    }

    void execute() throws Exception {
        command.execute();
    }

    abstract void run();

}
