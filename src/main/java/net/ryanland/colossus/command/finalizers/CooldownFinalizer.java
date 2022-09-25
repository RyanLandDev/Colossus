package net.ryanland.colossus.command.finalizers;

import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.events.command.BasicCommandEvent;

public class CooldownFinalizer implements Finalizer {

    @Override
    public void finalize(BasicCommandEvent event) {
        if (event.getCommand().hasCooldown()) {
            CooldownHandler.newCooldown(event);
        }
    }
}
