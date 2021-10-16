package net.ryanland.colossus.command.inhibitors;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.events.ContentCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class PermissionInhibitor implements Inhibitor {

    @Override
    public boolean check(ContentCommandEvent event) {
        return !event.getMember().hasPermission(event.getCommand().getPermission());
    }

    @Override
    public PresetBuilder buildMessage(ContentCommandEvent event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Insufficient Permissions", "You do not have permission to execute this command."
        );
    }
}
