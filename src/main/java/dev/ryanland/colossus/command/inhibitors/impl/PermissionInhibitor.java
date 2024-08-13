package dev.ryanland.colossus.command.inhibitors.impl;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.inhibitors.Inhibitor;
import dev.ryanland.colossus.events.command.BasicCommandEvent;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;

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
