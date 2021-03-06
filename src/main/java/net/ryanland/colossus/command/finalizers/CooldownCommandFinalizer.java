package net.ryanland.colossus.command.finalizers;

import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.events.CommandEvent;

public class CooldownCommandFinalizer implements CommandFinalizer {

    @Override
    public void finalize(CommandEvent event) {
        if (event.getCommand().hasCooldown()) {
            CooldownHandler.newCooldown(event);
        }
    }
}
