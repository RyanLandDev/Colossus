package net.ryanland.colossus.command.arguments.parsing;

import net.ryanland.colossus.events.CommandEvent;

public abstract sealed class ArgumentParser permits MessageCommandArgumentParser, SlashCommandArgumentParser {

    final CommandEvent event;

    public ArgumentParser(CommandEvent event) {
        this.event = event;
    }

    //TODO remove
    public CommandEvent event() {
        return event;
    }

    public abstract boolean parseArguments();
}
