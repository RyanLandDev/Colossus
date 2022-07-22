package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.inhibitors.CommandInhibitor;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class DisabledCommandInhibitor implements CommandInhibitor {

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
