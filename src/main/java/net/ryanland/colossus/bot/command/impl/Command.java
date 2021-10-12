package net.ryanland.colossus.bot.command.impl;

import net.ryanland.colossus.bot.command.arguments.ArgumentSet;
import net.ryanland.colossus.bot.command.executor.CommandHandler;
import net.ryanland.colossus.bot.command.executor.data.DisabledCommandHandler;
import net.ryanland.colossus.bot.command.executor.exceptions.CommandException;
import net.ryanland.colossus.bot.command.info.Category;
import net.ryanland.colossus.bot.command.info.CommandInfo;
import net.ryanland.colossus.bot.command.permissions.Permission;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.util.file.StorageType;

import java.util.List;

public abstract class Command {

    public abstract CommandInfo getInfo();

    // CommandData getters --------------------

    public final String getName() {
        return getInfo().getName();
    }

    public final String getUppercaseName() {
        return getName().substring(0, 1).toUpperCase() + getName().substring(1);
    }

    public final String getDescription() {
        return getInfo().getDescription();
    }

    public final Category getCategory() {
        return getInfo().getCategory();
    }

    public final Permission getPermission() {
        return getInfo().getPermission();
    }

    public final boolean hasCooldown() {
        return getCooldown() != 0;
    }

    public final int getCooldown() {
        return getInfo().getCooldown();
    }

    public final int getCooldownInMs() {
        return getCooldown() * 1000;
    }

    public final StorageType getCooldownStorageType() {
        return getInfo().getCooldownStorageType();
    }

    public final List<SubCommand> getSubCommands() {
        return getInfo().getSubCommands();
    }

    public final boolean isDisabled() {
        return DisabledCommandHandler.getInstance().isDisabled(this);
    }

    public static Command of(String alias) {
        return CommandHandler.getCommand(alias);
    }

    // ---------------------------------------------------------

    public abstract ArgumentSet getArguments();

    public abstract void run(CommandEvent event) throws CommandException;
}
