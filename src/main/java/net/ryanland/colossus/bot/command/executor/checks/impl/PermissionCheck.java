package net.ryanland.colossus.bot.command.executor.checks.impl;

import net.ryanland.colossus.bot.command.executor.checks.CommandCheck;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.util.message.builders.PresetBuilder;
import net.ryanland.colossus.util.message.builders.PresetType;

public class PermissionCheck extends CommandCheck {

    @Override
    public boolean check(CommandEvent event) {
        return !event.getCommand().getPermission().hasPermission(event.getMember());
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(
            PresetType.ERROR, "You do not have permission to execute this command.", "Insufficient Permissions"
        );
    }
}
