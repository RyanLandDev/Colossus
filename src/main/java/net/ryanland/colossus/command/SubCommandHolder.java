package net.ryanland.colossus.command;

import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.events.ContentCommandEvent;

public abstract class SubCommandHolder extends Command {

    @Override
    public final ArgumentSet getArguments() {
        return new ArgumentSet();
    }

    @Override
    public final void run(ContentCommandEvent event) {

    }

}
