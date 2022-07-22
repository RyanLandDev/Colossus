package net.ryanland.colossus.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.command.context.ContextCommandBuilder;
import net.ryanland.colossus.command.context.ContextCommandType;
import net.ryanland.colossus.command.cooldown.CooldownManager;
import net.ryanland.colossus.command.cooldown.MemoryCooldownManager;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import net.ryanland.colossus.command.permissions.PermissionHolder;
import net.ryanland.colossus.events.ContextCommandEvent;

import java.lang.reflect.ParameterizedType;

/**
 * All context commands extend this class
 * @param <T> Type of context command; must be either {@link User} or {@link Message}
 */
public abstract class ContextCommand<T> {

    private ContextCommandBuilder getInfo() {
        return getClass().getAnnotation(ContextCommandBuilder.class);
    }

    public final String getName() {
        return getInfo().name();
    }

    @SuppressWarnings("all")
    public final ContextCommandType getType() {
        return ContextCommandType.of((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public final String getUppercaseName() {
        return getName().substring(0, 1).toUpperCase() + getName().substring(1);
    }

    public final boolean hasCooldown() {
        return getCooldown() != 0;
    }

    public final int getCooldown() {
        return getInfo().cooldown();
    }

    public final int getCooldownInMs() {
        return getCooldown() * 1000;
    }

    public CooldownManager getCooldownManager() {
        return MemoryCooldownManager.getInstance();
    }

    public final boolean isDisabled() {
        return DisabledCommandHandler.getInstance().isDisabled(this);
    }

    public PermissionHolder getPermission() {
        return new PermissionHolder();
    }

    public final boolean memberHasPermission(Member member) {
        return getPermission().check(member);
    }

    public abstract void run(ContextCommandEvent<T> event) throws CommandException;

    public static ContextCommand<?> of(ContextCommandType type, String alias) {
        return CommandHandler.getContextCommand(type, alias);
    }
}
