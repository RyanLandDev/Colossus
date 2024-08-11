package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.ryanland.colossus.sys.util.DateUtil;

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
