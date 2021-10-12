package net.ryanland.colossus.bot.command.info;

import net.ryanland.colossus.bot.command.impl.SubCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SubCommandGroup {
    private final String name;
    private final String description;
    private final List<SubCommand> subCommands;
    private final HashMap<String, SubCommand> subCommandMap = new HashMap<>();

    public SubCommandGroup(@NotNull String name, @NotNull String description, @NotNull SubCommand... subCommands) {
        this.name = name;
        this.description = description;
        this.subCommands = Arrays.asList(subCommands);
        for (SubCommand cmd : subCommands) {
            subCommandMap.put(cmd.getName(), cmd);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public SubCommand getSubCommand(@NotNull String name) {
        return subCommandMap.get(name);
    }
}
