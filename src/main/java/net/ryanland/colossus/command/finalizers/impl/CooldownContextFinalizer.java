package net.ryanland.colossus.command.finalizers.impl;

import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.command.finalizers.ContextFinalizer;
import net.ryanland.colossus.events.command.ContextCommandEvent;

public class CooldownContextFinalizer implements ContextFinalizer {

    @Override
    public void finalize(ContextCommandEvent<?> event) {
        if (event.getCommand().hasCooldown()) {
            CooldownHandler.newCooldown(event);
        }
    }
}
