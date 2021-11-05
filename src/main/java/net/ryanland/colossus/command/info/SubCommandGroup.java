package net.ryanland.colossus.command.info;

import net.ryanland.colossus.command.SubCommand;


public abstract class SubCommandGroup {

    public abstract String getName();

    public abstract String getDescription();

    public abstract SubCommand[] getSubCommands();

    public SubCommand getSubCommand(String name) {
        for (SubCommand c : getSubCommands()) {
            if (c.getName().equals(name)) return c;
        }
        throw new IllegalArgumentException();
    }
}
