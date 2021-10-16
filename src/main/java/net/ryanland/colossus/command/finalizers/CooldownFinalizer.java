package net.ryanland.colossus.command.finalizers;

import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.events.ContentCommandEvent;

public class CooldownFinalizer implements Finalizer {

    @Override
    public void finalize(ContentCommandEvent event) {
        if (event.getCommand().hasCooldown()) {
            CooldownHandler.newCooldown(event);
        }
    }
}
