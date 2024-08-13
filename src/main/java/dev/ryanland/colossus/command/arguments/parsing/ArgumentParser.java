package dev.ryanland.colossus.command.arguments.parsing;

import dev.ryanland.colossus.events.command.CommandEvent;

public abstract sealed class ArgumentParser permits MessageCommandArgumentParser, SlashCommandArgumentParser {

    final CommandEvent event;

    public ArgumentParser(CommandEvent event) {
        this.event = event;
    }

    public abstract boolean parseArguments();
}
