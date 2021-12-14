package net.ryanland.colossus.command.permissions;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

/**
 * Helper implementation of {@link PermissionRequirement} for checking if a member has a {@link Permission}.
 * @see PermissionHolder
 * @see PermissionRequirement
 * @see PermissionBuilder
 */
public class JDAPermissionRequirement implements PermissionRequirement {

    private final Permission permission;

    public JDAPermissionRequirement(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean check(Member member) {
        return member.hasPermission(permission);
    }

    @Override
    public String getName() {
        return permission.getName();
    }
}
