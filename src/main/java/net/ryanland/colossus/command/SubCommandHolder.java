package net.ryanland.colossus.command;

import net.ryanland.colossus.command.arguments.ArgumentSet;

public abstract non-sealed class SubCommandHolder extends Command {

    @Override
    public final ArgumentSet getArguments() {
        return new ArgumentSet();
    }

}
