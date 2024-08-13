package dev.ryanland.colossus.command.permission.impl;

import dev.ryanland.colossus.command.permission.PermissionRequirement;
import dev.ryanland.colossus.command.permission.PermissionHolder;
import dev.ryanland.colossus.events.command.BasicCommandEvent;

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
