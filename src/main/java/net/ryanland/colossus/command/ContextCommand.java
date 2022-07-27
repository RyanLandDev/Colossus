package net.ryanland.colossus.command;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.command.context.ContextCommandBuilder;
import net.ryanland.colossus.command.context.ContextCommandType;
import net.ryanland.colossus.command.cooldown.CooldownManager;
import net.ryanland.colossus.command.cooldown.MemoryCooldownManager;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import net.ryanland.colossus.command.permission.PermissionHolder;
import net.ryanland.colossus.events.command.ContextCommandEvent;

import java.lang.reflect.ParameterizedType;

/**
 * All context commands extend this class
 * @param <T> Type of context command; must be either {@link User} or {@link Message}
 */
public abstract non-sealed class ContextCommand<T extends ISnowflake> extends BasicCommand {

    private ContextCommandBuilder getInfo() {
        return getClass().getAnnotation(ContextCommandBuilder.class);
    }

    @Override
    public final String getName() {
        return getInfo().name();
    }

    @SuppressWarnings("all")
    public final ContextCommandType getType() {
        return ContextCommandType.of((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Override
    public final CommandType getCommandType() {
        return getType().getCommandType();
    }

    @Override
    public final boolean hasCooldown() {
        return getCooldown() != 0;
    }

    @Override
    public final float getCooldown() {
        return getInfo().cooldown();
    }

    @Override
    public CooldownManager getCooldownManager() {
        return MemoryCooldownManager.getInstance();
    }

    @Override
    public final boolean isGuildOnly() {
        return getInfo().guildOnly();
    }

    @Override
    public final boolean canBeDisabled() {
        return getInfo().canBeDisabled();
    }

    @Override
    public final boolean isDisabled() {
        return DisabledCommandHandler.getInstance().isDisabled(this);
    }

    @Override
    public PermissionHolder getPermission() {
        return new PermissionHolder();
    }

    public abstract void run(ContextCommandEvent<T> event) throws CommandException;

    public static ContextCommand<?> of(ContextCommandType type, String alias) {
        return CommandHandler.getContextCommand(type, alias);
    }
}
