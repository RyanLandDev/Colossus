package net.ryanland.colossus.command;

import net.dv8tion.jda.api.Permission;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.cooldown.CooldownManager;
import net.ryanland.colossus.command.cooldown.MemoryCooldownManager;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import net.ryanland.colossus.command.info.Category;
import net.ryanland.colossus.command.info.SubCommandGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.ryanland.colossus.command.info.HelpMaker.getInfo;

public abstract class Command  {

    public Command() {}

    // CommandData getters --------------------

    public final String getName() {
        return getInfo(this).name();
    }

    public final String getUppercaseName() {
        return getName().substring(0, 1).toUpperCase() + getName().substring(1);
    }

    public final String getDescription() {
        return getInfo(this).description();
    }

    public final Category getCategory() {
        return getInfo(this).category();
    }

    public final Permission getPermission() {
        return getInfo(this).permission();
    }

    public final boolean hasCooldown() {
        return getCooldown() != 0;
    }

    public final int getCooldown() {
        return getInfo(this).cooldown();
    }

    public final int getCooldownInMs() {
        return getCooldown() * 1000;
    }

    public final boolean isGuildOnly() {
        return getInfo(this).guildOnly();
    }

    public final CooldownManager getCooldownManager() {
        return MemoryCooldownManager.getInstance();
    }

    public final List<SubCommand> getSubCommands() {
        List<SubCommand> list = new ArrayList<>();
        for (Class<? extends SubCommand> c : getInfo(this).subcommands()) {
            try {
                SubCommand sc = c.getDeclaredConstructor().newInstance();
                list.add(sc);
            } catch (Exception ignored) {}
        }
        return list;
    }

    public final List<SubCommandGroup> getSubCommandGroups() {
        List<SubCommandGroup> list = new ArrayList<>();
        for (Class<? extends SubCommandGroup> c : getInfo(this).subcommandgroups()) {
            try {
                SubCommandGroup sc = c.getDeclaredConstructor().newInstance();
                list.add(sc);
            } catch (Exception ignored) {}
        }
        return list;
    }

    public final HashMap<String, SubCommand> getSubCommandMap() {
        HashMap<String, SubCommand> map = new HashMap<>();
        for (SubCommand cmd : getSubCommands()) {
            map.put(cmd.getName(), cmd);
        }
        return map;
    }

    public final HashMap<String, SubCommandGroup> getSubCommandGroupMap() {
        HashMap<String, SubCommandGroup> map = new HashMap<>();
        for (SubCommandGroup group : getSubCommandGroups()) {
            map.put(group.getName(), group);
        }
        return map;
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
