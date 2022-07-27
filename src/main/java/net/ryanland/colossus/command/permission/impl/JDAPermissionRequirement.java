package net.ryanland.colossus.command.permission.impl;

import net.dv8tion.jda.api.Permission;
import net.ryanland.colossus.command.permission.MemberPermissionRequirement;
import net.ryanland.colossus.command.permission.PermissionBuilder;
import net.ryanland.colossus.command.permission.PermissionHolder;
import net.ryanland.colossus.command.permission.PermissionRequirement;
import net.ryanland.colossus.sys.entities.ColossusMember;

/**
 * Helper implementation of {@link PermissionRequirement} for checking if a member has a {@link Permission}.
 *
 * @see PermissionHolder
 * @see PermissionRequirement
 * @see PermissionBuilder
 */
public record JDAPermissionRequirement(Permission permission) implements MemberPermissionRequirement {

    @Override
    public boolean check(ColossusMember entity) {
        return entity.hasPermission(permission);
    }

    @Override
    public String getName() {
        return permission.getName();
    }
}
