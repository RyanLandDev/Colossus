package net.ryanland.colossus.command.permission.impl;

import net.dv8tion.jda.api.Permission;
import net.ryanland.colossus.command.permission.PermissionBuilder;
import net.ryanland.colossus.command.permission.PermissionHolder;
import net.ryanland.colossus.command.permission.PermissionRequirement;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusMember;

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
