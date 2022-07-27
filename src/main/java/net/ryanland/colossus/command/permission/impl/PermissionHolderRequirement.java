package net.ryanland.colossus.command.permission.impl;

import net.ryanland.colossus.command.permission.PermissionHolder;
import net.ryanland.colossus.command.permission.PermissionRequirement;
import net.ryanland.colossus.events.command.BasicCommandEvent;

/**
 * Implementation of {@link PermissionRequirement} for nested holders
 */
public record PermissionHolderRequirement(PermissionHolder holder) implements PermissionRequirement {

    @Override
    public boolean check(BasicCommandEvent event) {
        return holder.check(event);
    }

    @Override
    public String getName() {
        return holder.getName();
    }

}
