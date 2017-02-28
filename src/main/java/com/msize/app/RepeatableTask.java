package com.msize.app;

abstract class RepeatableTask {

    private Command command;

    RepeatableTask(Command command) {
        this.command = command;
    }

    void execute() throws Exception {
        command.execute();
    }

    abstract void run();

}
