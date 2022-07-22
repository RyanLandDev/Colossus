package net.ryanland.colossus.command;

import net.dv8tion.jda.api.entities.Member;
import net.ryanland.colossus.command.cooldown.CooldownManager;
import net.ryanland.colossus.command.permissions.PermissionHolder;

public abstract sealed class BasicCommand permits Command, ContextCommand {

    public abstract String getName();

    public final String getUppercaseName() {
        return getName().substring(0, 1).toUpperCase() + getName().substring(1);
    }

    public abstract CommandType getCommandType();

    public abstract boolean hasCooldown();

    public abstract int getCooldown();

    public final int getCooldownInMs() {
        return getCooldown() * 1000;
    }

    public abstract CooldownManager getCooldownManager();

    public abstract boolean canBeDisabled();

    public abstract boolean isDisabled();

    public abstract PermissionHolder getPermission();

    public final boolean memberHasPermission(Member member) {
        return getPermission().check(member);
    }
}
