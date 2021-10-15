package net.ryanland.colossus.bot.command.finalizers;

import net.ryanland.colossus.bot.command.cooldown.CooldownHandler;
import net.ryanland.colossus.bot.events.CommandEvent;

public class CooldownFinalizer implements Finalizer {

    @Override
    public void finalize(CommandEvent event) {
        if (event.getCommand().hasCooldown()) {
            CooldownHandler.newCooldown(event);
        }
    }
}
