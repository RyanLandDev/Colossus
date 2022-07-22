package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.inhibitors.ContextInhibitor;
import net.ryanland.colossus.events.ContextCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class DisabledContextInhibitor implements ContextInhibitor {

    @Override
    public boolean check(ContextCommandEvent<?> event) {
        return event.getCommand().isDisabled();
    }

    @Override
    public PresetBuilder buildMessage(ContextCommandEvent<?> event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Disabled", "This context command is currently disabled."
        );
    }
}
