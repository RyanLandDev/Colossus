package net.ryanland.colossus.bot.command.inhibitors;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class PermissionInhibitor implements Inhibitor {

    @Override
    public boolean check(CommandEvent event) {
        return !event.getMember().hasPermission(event.getCommand().getPermission());
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Insufficient Permissions", "You do not have permission to execute this command."
        );
    }
}
