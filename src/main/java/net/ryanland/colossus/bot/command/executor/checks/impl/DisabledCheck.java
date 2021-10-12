package net.ryanland.colossus.bot.command.executor.checks.impl;

import net.ryanland.colossus.bot.command.executor.checks.CommandCheck;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.util.message.builders.PresetBuilder;
import net.ryanland.colossus.util.message.builders.PresetType;

public class DisabledCheck extends CommandCheck {

    @Override
    public boolean check(CommandEvent event) {
        return event.getCommand().isDisabled();
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(
            PresetType.ERROR, "This command is currently disabled.", "Disabled"
        );
    }
}
