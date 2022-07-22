package net.ryanland.colossus.command.finalizers;

import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.ContextCommandEvent;

public class CooldownContextFinalizer implements ContextFinalizer {

    @Override
    public void finalize(ContextCommandEvent<?> event) {
        if (event.getCommand().hasCooldown()) {
            CooldownHandler.newCooldown(event);
        }
    }
}
