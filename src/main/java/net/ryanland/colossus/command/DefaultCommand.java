package net.ryanland.colossus.command;

import net.ryanland.colossus.command.permissions.PermissionHolder;

/**
 * Default, uncategorized command
 */
public abstract non-sealed class DefaultCommand extends Command {

    @Override
    public PermissionHolder getPermission() {
        return new PermissionHolder();
    }

    @Override
    public Category getCategory() {
        return new Category("Uncategorized", "These are all uncategorized commands.", "🍥");
    }
}
