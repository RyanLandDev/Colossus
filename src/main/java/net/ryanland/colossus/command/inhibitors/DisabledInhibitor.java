package net.ryanland.colossus.command.inhibitors;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.events.ContentCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class DisabledInhibitor extends Inhibitor {

    @Override
    public boolean check(ContentCommandEvent event) {
        return event.getCommand().isDisabled();
    }

    @Override
    public PresetBuilder buildMessage(ContentCommandEvent event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Disabled", "This command is currently disabled."
        );
    }
}
