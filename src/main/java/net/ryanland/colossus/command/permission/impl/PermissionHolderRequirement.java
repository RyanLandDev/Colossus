package net.ryanland.colossus.command.permission.impl;

import net.ryanland.colossus.command.permission.PermissionHolder;
import net.ryanland.colossus.command.permission.PermissionRequirement;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusEntity;

/**
 * Implementation of {@link PermissionRequirement} for nested holders
 */
public record PermissionHolderRequirement(PermissionHolder holder) implements PermissionRequirement<ColossusEntity> {

    @Override
    public boolean check(BasicCommandEvent event) {
        return holder.check(event);
    }

    @Override
    public String getName() {
        return holder.getName();
    }

    // unused methods
    @Override
    public ColossusEntity fromCommandEvent(BasicCommandEvent event) {
        throw new IllegalStateException();
    }

    @Override
    public boolean check(ColossusEntity entity) {
        throw new IllegalStateException();
    }
}
