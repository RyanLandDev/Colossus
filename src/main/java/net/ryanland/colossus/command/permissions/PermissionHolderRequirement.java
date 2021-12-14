package net.ryanland.colossus.command.permissions;

import net.dv8tion.jda.api.entities.Member;

/**
 * Implementation of {@link PermissionRequirement} for nested holders
 */
public class PermissionHolderRequirement implements PermissionRequirement {

    private final PermissionHolder holder;

    public PermissionHolderRequirement(PermissionHolder holder) {
        this.holder = holder;
    }

    @Override
    public boolean check(Member member) {
        return holder.check(member);
    }

    @Override
    public String getName() {
        return holder.getName();
    }
}
