package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class GuildOnlyInhibitor implements Inhibitor {

    @Override
    public boolean check(BasicCommandEvent event) {
        if (event.getCommand().isGuildOnly())
            return !event.isFromGuild();
        else return false;
    }

    @Override
    public PresetBuilder buildMessage(BasicCommandEvent event) {
        return new PresetBuilder(
                Colossus.getErrorPresetType(),
                "Server Only", "This command can only be executed in servers."
        );
    }
}
