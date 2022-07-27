package net.ryanland.colossus.command.finalizers.impl;

import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.command.finalizers.CommandFinalizer;
import net.ryanland.colossus.events.command.CommandEvent;

public class CooldownCommandFinalizer implements CommandFinalizer {

    @Override
    public void finalize(CommandEvent event) {
        if (event.getCommand().hasCooldown()) {
            CooldownHandler.newCooldown(event);
        }
    }
}
