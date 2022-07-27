package net.ryanland.colossus.command;

import net.ryanland.colossus.command.permission.PermissionHolder;

/**
 * All command implementation classes extend this class
 */
public abstract non-sealed class BaseCommand extends Command {

    @Override
    public PermissionHolder getPermission() {
        return new PermissionHolder();
    }

}
