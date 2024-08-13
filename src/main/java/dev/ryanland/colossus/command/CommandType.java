package dev.ryanland.colossus.command;

import dev.ryanland.colossus.command.executor.CommandHandler;

import java.util.function.Function;

public enum CommandType {
    REGULAR(0, "Regular", CommandHandler::getCommand),
    CONTEXT_USER(1, "User", CommandHandler::getUserContextCommand),
    CONTEXT_MESSAGE(2, "Message", CommandHandler::getMessageContextCommand);

    private final int id;
    private final String name;
    private final Function<String, BasicCommand> commandGetter;

    CommandType(int id, String name, Function<String, BasicCommand> commandGetter) {
        this.id = id;
        this.name = name;
        this.commandGetter = commandGetter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Function<String, BasicCommand> getCommandGetter() {
        return commandGetter;
    }

    public BasicCommand getCommand(String name) {
        return getCommandGetter().apply(name);
    }

    public static CommandType of(int id) {
        for (CommandType type : values()) {
            if (type.getId() == id) return type;
        }
        return null;
    }
}
