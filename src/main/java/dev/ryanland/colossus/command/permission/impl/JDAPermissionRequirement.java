package dev.ryanland.colossus.command.permission.impl;

import dev.ryanland.colossus.command.permission.PermissionBuilder;
import dev.ryanland.colossus.command.permission.PermissionRequirement;
import net.dv8tion.jda.api.Permission;
import dev.ryanland.colossus.command.permission.PermissionHolder;
import dev.ryanland.colossus.events.command.BasicCommandEvent;

/**
 * Helper implementation of {@link PermissionRequirement} for checking if a member has a {@link Permission}.
 *
 * @see PermissionHolder
 * @see PermissionRequirement
 * @see PermissionBuilder
 */
public record JDAPermissionRequirement(Permission permission) implements PermissionRequirement {

    @Override
    public boolean check(BasicCommandEvent event) {
        return event.getMember().hasPermission(permission);
    }

    @Override
    public String getName() {
        return permission.getName();
    }
}
