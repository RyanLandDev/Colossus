package dev.ryanland.colossus.command.inhibitors.impl;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.inhibitors.Inhibitor;
import dev.ryanland.colossus.events.command.BasicCommandEvent;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;

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
