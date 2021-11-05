package net.ryanland.colossus.command.arguments.parsing;

import net.ryanland.colossus.events.CommandEvent;

public abstract class ArgumentParser {

    final CommandEvent event;

    public ArgumentParser(CommandEvent event) {
        this.event = event;
    }

    public abstract boolean parseArguments();
}
