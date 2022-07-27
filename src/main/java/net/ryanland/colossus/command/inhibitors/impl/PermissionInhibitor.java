package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class PermissionInhibitor implements Inhibitor {

    @Override
    public boolean check(BasicCommandEvent event) {
        return !event.getCommand().getPermission().check(event);
    }

    @Override
    public PresetBuilder buildMessage(BasicCommandEvent event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Insufficient Permissions", "You do not have permission to execute this command."
        );
    }
}
