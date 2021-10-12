package net.ryanland.colossus.bot.command.executor.checks.impl;

import net.ryanland.colossus.bot.command.executor.checks.CommandCheck;
import net.ryanland.colossus.bot.command.executor.cooldown.CooldownHandler;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.sys.message.builders.PresetBuilder;
import net.ryanland.colossus.sys.message.builders.PresetType;

import java.util.Date;

public class CooldownCheck extends CommandCheck {

    @Override
    public boolean check(CommandEvent event) {
        return event.getCommand().hasCooldown() && CooldownHandler.isCooldownActive(event);
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(
            PresetType.ERROR,
            "This command is currently on cooldown.\nTime left: " +
                DateUtil.formatRelative(new Date(
                    CooldownHandler.getActiveCooldown(event).expires().getTime() - System.currentTimeMillis())),
            "On Cooldown"
        );
    }
}
