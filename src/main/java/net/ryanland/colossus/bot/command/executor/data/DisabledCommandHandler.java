package net.ryanland.colossus.bot.command.executor.data;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.bot.command.executor.exceptions.CommandException;
import net.ryanland.colossus.bot.command.impl.Command;

import java.util.ArrayList;
import java.util.List;

public class DisabledCommandHandler {

    private static DisabledCommandHandler instance;

    public DisabledCommandHandler() {
        instance = this;
    }

    public static DisabledCommandHandler getInstance() {
        return instance == null ? new DisabledCommandHandler() : instance;
    }

    public List<Command> getDisabledCommands() {
        return new ArrayList<>();
        //TODO return EventsBot.getGlobalDocument().getDisabledCommands();
    }

    public List<String> getDisabledCommandsRaw() {
        return Colossus.getGlobalDocument().getDisabledCommandsRaw();
    }

    public boolean isDisabled(Command command) {
        return getDisabledCommands().contains(command);
    }

    public void enable(Command command) throws CommandException {
        enable(command.getName());
    }

    public void enable(String command) throws CommandException {
        List<String> disabled = getDisabledCommandsRaw();
        if (!disabled.contains(command)) {
            throw new CommandException("This command is already enabled.");
        }
        disabled.remove(command);

        Colossus.getGlobalDocument()
            .setDisabledCommandsRaw(disabled)
            .update();
    }

    public void disable(Command command) throws CommandException {
        disable(command.getName());
    }

    public void disable(String command) throws CommandException {
        List<String> disabled = getDisabledCommandsRaw();
        if (disabled.contains(command)) {
            throw new CommandException("This command is already disabled.");
        }
        disabled.add(command);

        Colossus.getGlobalDocument()
            .setDisabledCommandsRaw(disabled)
            .update();
    }

}
