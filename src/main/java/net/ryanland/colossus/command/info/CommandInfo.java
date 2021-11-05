package net.ryanland.colossus.command.info;

import net.dv8tion.jda.api.Permission;
import net.ryanland.colossus.command.SubCommand;
import net.ryanland.colossus.command.cooldown.CooldownManager;
import net.ryanland.colossus.command.cooldown.MemoryCooldownManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class CommandInfo {

    //TODO remove this class and replace it completely with the CommandBuilder annotation
    private String name;
    private String description;
    private Category category;
    private Permission permission = Permission.MESSAGE_WRITE;
    private int cooldown;
    private CooldownManager cooldownManager = MemoryCooldownManager.getInstance();

    private List<SubCommand> subCommands = new ArrayList<>();
    private List<SubCommandGroup> subCommandGroups = new ArrayList<>();
    private HashMap<String, SubCommand> subCommandMap;
    private HashMap<String, SubCommandGroup> subCommandGroupMap;

    public CommandInfo name(String name) {
        this.name = name;
        return this;
    }

    public CommandInfo description(String description) {
        this.description = description;
        return this;
    }

    public CommandInfo category(Category category) {
        this.category = category;
        return this;
    }

    public CommandInfo permission(Permission permission) {
        this.permission = permission;
        return this;
    }

    /**
     * The cooldown set for this command.
     * @param cooldown Cooldown in seconds.
     * @return The {@link CommandInfo} for chaining.
     * @see #cooldownManager(CooldownManager)
     */
    public CommandInfo cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public CommandInfo cooldownManager(CooldownManager cooldownStorageType) {
        this.cooldownManager = cooldownStorageType;
        return this;
    }

    public CommandInfo subCommands(SubCommand... subCommands) {
        this.subCommands = Arrays.asList(subCommands);
        subCommandMap = new HashMap<>();
        for (SubCommand cmd : subCommands) {
            subCommandMap.put(cmd.getName(), cmd);
        }
        return this;
    }

    public CommandInfo subCommandGroups(SubCommandGroup... subCommandGroups) {
        this.subCommandGroups = Arrays.asList(subCommandGroups);
        subCommandGroupMap = new HashMap<>();
        for (SubCommandGroup group : subCommandGroups) {
            subCommandGroupMap.put(group.getName(), group);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Permission getPermission() {
        return permission;
    }

    public int getCooldown() {
        return cooldown;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public List<SubCommandGroup> getSubCommandGroups() {
        return subCommandGroups;
    }

    public HashMap<String, SubCommand> getSubCommandMap() {
        return subCommandMap;
    }

    public HashMap<String, SubCommandGroup> getSubCommandGroupMap() {
        return subCommandGroupMap;
    }

}
