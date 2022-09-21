package net.ryanland.colossus.command.executor;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.CommandException;

import java.util.List;

public class DisabledCommandHandler {

    private static final DisabledCommandHandler INSTANCE = new DisabledCommandHandler();
    public static final String DISABLED_COMMANDS_KEY = "disabled_commands";

    public static DisabledCommandHandler getInstance() {
        return INSTANCE;
    }

    public List<BasicCommand> getDisabledCommands() {
        return Colossus.getGlobalSupply().get(DISABLED_COMMANDS_KEY);
    }

    public boolean isDisabled(BasicCommand command) {
        return getDisabledCommands().contains(command);
    }

    public void enable(BasicCommand command) throws CommandException {
        List<BasicCommand> disabled = getDisabledCommands();
        if (!disabled.contains(command)) throw new CommandException("This command is already enabled.");
        disabled.remove(command);
        Colossus.getGlobalSupply().push(DISABLED_COMMANDS_KEY, disabled);
    }

    public void disable(BasicCommand command) throws CommandException {
        List<BasicCommand> disabled = getDisabledCommands();
        if (disabled.contains(command)) throw new CommandException("This command is already disabled.");
        if (!command.canBeDisabled()) throw new CommandException("This command cannot be disabled.");
        disabled.add(command);
        Colossus.getGlobalSupply().push(DISABLED_COMMANDS_KEY, disabled);
    }

}
