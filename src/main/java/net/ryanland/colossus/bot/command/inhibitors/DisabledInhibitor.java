package net.ryanland.colossus.bot.command.inhibitors;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class DisabledInhibitor extends Inhibitor {

    @Override
    public boolean check(CommandEvent event) {
        return event.getCommand().isDisabled();
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "Disabled", "This command is currently disabled."
        );
    }
}
