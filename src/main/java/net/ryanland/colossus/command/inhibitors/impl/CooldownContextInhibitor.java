package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.command.inhibitors.ContextInhibitor;
import net.ryanland.colossus.events.ContextCommandEvent;
import net.ryanland.colossus.sys.util.DateUtil;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Date;

public class CooldownContextInhibitor implements ContextInhibitor {

    @Override
    public boolean check(ContextCommandEvent<?> event) {
        return event.getCommand().hasCooldown() && CooldownHandler.isCooldownActive(event);
    }

    @Override
    public PresetBuilder buildMessage(ContextCommandEvent<?> event) {
        return new PresetBuilder(Colossus.getErrorPresetType())
            .setTitle("On Cooldown")
            .setDescription("This command is currently on cooldown.\nTime left: " +
                DateUtil.formatRelative(new Date(
                    CooldownHandler.getActiveCooldown(event).expires().getTime() - System.currentTimeMillis())));
    }
}
