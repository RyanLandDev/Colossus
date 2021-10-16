package net.ryanland.colossus.command;

import net.dv8tion.jda.api.Permission;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.cooldown.CooldownManager;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import net.ryanland.colossus.command.info.Category;
import net.ryanland.colossus.command.info.CommandInfo;

import java.util.List;

public abstract class Command {

    private CommandInfo info;

    public CommandInfo getInfo() {
        return info;
    }

    public void setInfo(CommandInfo info) {
        this.info = info;
    }

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

    public final CooldownManager getCooldownManager() {
        return getInfo().getCooldownManager();
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
}
