package net.threadly.core.command;

import java.lang.reflect.Method;

public class CommandSpec {
    private String path;
    private Command command;
    private Method method;

    public CommandSpec(String path, Method method, Command command) {
        this.command = command;
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public Method getMethod() {
        return method;
    }

    public Command getCommand() {
        return command;
    }
}
