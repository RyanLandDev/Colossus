package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.inhibitors.CommandInhibitor;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class PermissionCommandInhibitor implements CommandInhibitor {

    @Override
    public boolean check(CommandEvent event) {
        return !event.getCommand().getPermission().check(event);
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Insufficient Permissions", "You do not have permission to execute this command."
        );
    }
}
