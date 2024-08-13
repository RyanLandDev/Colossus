package dev.ryanland.colossus.command.inhibitors.impl;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.inhibitors.Inhibitor;
import dev.ryanland.colossus.events.command.BasicCommandEvent;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;

public class DisabledInhibitor implements Inhibitor {

    @Override
    public boolean check(BasicCommandEvent event) {
        return event.getCommand().isDisabled();
    }

    @Override
    public PresetBuilder buildMessage(BasicCommandEvent event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Disabled", "This command is currently disabled."
        );
    }
}
