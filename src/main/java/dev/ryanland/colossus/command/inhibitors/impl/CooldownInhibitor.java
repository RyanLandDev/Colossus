package dev.ryanland.colossus.command.inhibitors.impl;

import dev.ryanland.colossus.command.inhibitors.Inhibitor;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.cooldown.CooldownHandler;
import dev.ryanland.colossus.events.command.BasicCommandEvent;
import dev.ryanland.colossus.sys.util.DateUtil;

import java.util.Date;

public class CooldownInhibitor implements Inhibitor {

    @Override
    public boolean check(BasicCommandEvent event) {
        return event.getCommand().hasCooldown() && CooldownHandler.isCooldownActive(event);
    }

    @Override
    public PresetBuilder buildMessage(BasicCommandEvent event) {
        return new PresetBuilder(Colossus.getErrorPresetType())
            .setTitle("On Cooldown")
            .setDescription("This command is currently on cooldown.\nTime left: " +
                DateUtil.formatRelative(new Date(
                    CooldownHandler.getActiveCooldown(event).expires().getTime() - System.currentTimeMillis())));
    }
}
