package dev.ryanland.colossus.command.finalizers;

import dev.ryanland.colossus.command.cooldown.CooldownHandler;
import dev.ryanland.colossus.events.command.BasicCommandEvent;

public class CooldownFinalizer implements Finalizer {

    @Override
    public void finalize(BasicCommandEvent event) {
        if (event.getCommand().hasCooldown()) {
            CooldownHandler.newCooldown(event);
        }
    }
}
