package net.ryanland.colossus.command.context;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.context.ContextInteraction;

public enum ContextCommandType {
    USER(Command.Type.USER, ContextInteraction.ContextTarget.USER, User.class),
    MESSAGE(Command.Type.MESSAGE, ContextInteraction.ContextTarget.MESSAGE, Message.class);

    private final Command.Type jdaEquivalent;
    private final ContextInteraction.ContextTarget target;
    private final Class<?> jdaClass;

    ContextCommandType(Command.Type jdaEquivalent, ContextInteraction.ContextTarget target, Class<?> jdaClass) {
        this.jdaEquivalent = jdaEquivalent;
        this.target = target;
        this.jdaClass = jdaClass;
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
