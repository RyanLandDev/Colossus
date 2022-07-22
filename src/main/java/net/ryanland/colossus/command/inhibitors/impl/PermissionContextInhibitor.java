package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.inhibitors.ContextInhibitor;
import net.ryanland.colossus.events.ContextCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class PermissionContextInhibitor implements ContextInhibitor {

    @Override
    public boolean check(ContextCommandEvent<?> event) {
        return !event.getCommand().memberHasPermission(event.getMember());
    }

    @Override
    public PresetBuilder buildMessage(ContextCommandEvent<?> event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Insufficient Permissions", "You do not have permission to execute this context command."
        );
    }
}
