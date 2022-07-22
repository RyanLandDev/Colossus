package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.command.inhibitors.CommandInhibitor;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.DateUtil;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Date;

public class CooldownCommandInhibitor implements CommandInhibitor {

    @Override
    public boolean check(CommandEvent event) {
        return event.getCommand().hasCooldown() && CooldownHandler.isCooldownActive(event);
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(Colossus.getErrorPresetType())
            .setTitle("On Cooldown")
            .setDescription("This command is currently on cooldown.\nTime left: " +
                DateUtil.formatRelative(new Date(
                    CooldownHandler.getActiveCooldown(event).expires().getTime() - System.currentTimeMillis())));
    }
}
