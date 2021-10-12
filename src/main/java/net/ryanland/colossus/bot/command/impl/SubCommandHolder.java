package net.ryanland.colossus.bot.command.impl;

import net.ryanland.colossus.bot.command.arguments.ArgumentSet;
import net.ryanland.colossus.bot.events.CommandEvent;

public abstract class SubCommandHolder extends Command {

    @Override
    public final ArgumentSet getArguments() {
        return new ArgumentSet();
    }

    @Override
    public final void run(CommandEvent event) {

    }

}
