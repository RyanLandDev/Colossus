package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;

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
