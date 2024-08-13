package dev.ryanland.colossus.command;

import dev.ryanland.colossus.command.permission.PermissionHolder;

/**
 * All command implementation classes extend this class
 */
public abstract non-sealed class BaseCommand extends Command {

    @Override
    public PermissionHolder getPermission() {
        return new PermissionHolder();
    }

}
