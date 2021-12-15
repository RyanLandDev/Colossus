package net.ryanland.colossus.command.info;

import net.ryanland.colossus.command.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo {

    private final SubCommandGroup group;


    public GroupInfo(SubCommandGroup group) {
        this.group = group;
    }

    public String getName() {
        return group.name();
    }

    public String getDescription() {
        return group.description();
    }

    public List<SubCommand> getSubCommands() {
        List<SubCommand> subCmds = new ArrayList<>();
        for (Class<? extends SubCommand> cmd : group.subcommands()) {
            try {
                subCmds.add(cmd.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subCmds;
    }

    public SubCommand getSubCommand(String name) {


        for (SubCommand c : getSubCommands()) {
            if (c.getName().equals(name)) return c;
        }
        throw new IllegalArgumentException();
    }
}
