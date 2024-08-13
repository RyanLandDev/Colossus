package dev.ryanland.colossus.command.context;

import dev.ryanland.colossus.command.CommandType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.context.ContextInteraction;

public enum ContextCommandType {
    USER("User", CommandType.CONTEXT_USER, Command.Type.USER, ContextInteraction.ContextTarget.USER, User.class),
    MESSAGE("Message", CommandType.CONTEXT_MESSAGE, Command.Type.MESSAGE, ContextInteraction.ContextTarget.MESSAGE, Message.class);

    private final String name;
    private final CommandType commandType;
    private final Command.Type jdaEquivalent;
    private final ContextInteraction.ContextTarget target;
    private final Class<?> jdaClass;

    ContextCommandType(String name, CommandType commandType, Command.Type jdaEquivalent,
                       ContextInteraction.ContextTarget target, Class<?> jdaClass) {
        this.name = name;
        this.commandType = commandType;
        this.jdaEquivalent = jdaEquivalent;
        this.target = target;
        this.jdaClass = jdaClass;
    }

    public String getName() {
        return name;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Command.Type getJDAEquivalent() {
        return jdaEquivalent;
    }

    public ContextInteraction.ContextTarget getTarget() {
        return target;
    }

    public Class<?> getJDAClass() {
        return jdaClass;
    }

    public int getId() {
        return getJDAEquivalent().getId();
    }

    public static ContextCommandType of(int id) {
        for (ContextCommandType type : values()) {
            if (type.getId() == id) return type;
        }
        return null;
    }

    public static ContextCommandType of(ContextInteraction.ContextTarget target) {
        for (ContextCommandType type : values()) {
            if (type.getTarget() == target) return type;
        }
        return null;
    }

    public static ContextCommandType of(Class<?> jdaClass) {
        for (ContextCommandType type : values()) {
            if (type.getJDAClass() == jdaClass) return type;
        }
        return null;
    }
}
