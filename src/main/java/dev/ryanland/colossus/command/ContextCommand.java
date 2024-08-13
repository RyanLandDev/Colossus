package dev.ryanland.colossus.command;

import dev.ryanland.colossus.command.context.ContextCommandBuilder;
import dev.ryanland.colossus.command.context.ContextCommandType;
import dev.ryanland.colossus.command.cooldown.CooldownManager;
import dev.ryanland.colossus.command.cooldown.MemoryCooldownManager;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.command.permission.PermissionHolder;
import dev.ryanland.colossus.events.command.ContextCommandEvent;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

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
        return getInfo().disabled();
    }

    @Override
    public final boolean isDisabled() {
        return getInfo().disabled();
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
