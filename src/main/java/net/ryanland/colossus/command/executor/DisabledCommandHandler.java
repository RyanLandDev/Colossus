package net.ryanland.colossus.command.executor;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.sys.file.serializer.CommandsSerializer;
import net.ryanland.colossus.sys.file.serializer.ContextCommandsSerializer;

import java.util.ArrayList;
import java.util.List;

public class DisabledCommandHandler {

    private static final DisabledCommandHandler INSTANCE = new DisabledCommandHandler();
    private static final String DISABLED_COMMANDS_KEY = "_dc";
    private static final String DISABLED_CONTEXT_COMMANDS_KEY = "_dcc";

    public static DisabledCommandHandler getInstance() {
        return INSTANCE;
    }

    public List<Command> getDisabledCommands() {
        return CommandsSerializer.getInstance().deserialize(Colossus.getGlobalTable().get(DISABLED_COMMANDS_KEY, new ArrayList<>()));
    }

    public List<ContextCommand<?>> getDisabledContextCommands() {
        return ContextCommandsSerializer.getInstance().deserialize(Colossus.getGlobalTable().get(DISABLED_CONTEXT_COMMANDS_KEY, new ArrayList<>()));
    }

    public boolean isDisabled(Command command) {
        return getDisabledCommands().contains(command);
    }

    public boolean isDisabled(ContextCommand<?> contextCommand) {
        return getDisabledContextCommands().contains(contextCommand);
    }

    public void enable(BasicCommand command) throws CommandException {
        if (command instanceof ContextCommand) enable((ContextCommand<?>) command);
        else if (command instanceof Command) enable((Command) command);
    }

    public void disable(BasicCommand command) throws CommandException {
        if (command instanceof ContextCommand) disable((ContextCommand<?>) command);
        else if (command instanceof Command) disable((Command) command);
    }

    public void enable(Command command) throws CommandException {
        List<Command> disabled = getDisabledCommands();
        if (!disabled.contains(command)) throw new CommandException("This command is already enabled.");
        disabled.remove(command);
        Colossus.getDatabaseDriver().modifyTable(Colossus.getSelfUser(),
            table -> table.put(DISABLED_COMMANDS_KEY, CommandsSerializer.getInstance().serialize(disabled)));
    }

    public void disable(Command command) throws CommandException {
        List<Command> disabled = getDisabledCommands();
        if (disabled.contains(command)) throw new CommandException("This command is already disabled.");
        disabled.add(command);
        Colossus.getDatabaseDriver().modifyTable(Colossus.getSelfUser(),
            table -> table.put(DISABLED_COMMANDS_KEY, CommandsSerializer.getInstance().serialize(disabled)));
    }

    public void enable(ContextCommand<?> contextCommand) throws CommandException {
        List<ContextCommand<?>> disabled = getDisabledContextCommands();
        if (!disabled.contains(contextCommand)) throw new CommandException("This command is already enabled.");
        disabled.remove(contextCommand);
        Colossus.getDatabaseDriver().modifyTable(Colossus.getSelfUser(),
            table -> table.put(DISABLED_CONTEXT_COMMANDS_KEY, ContextCommandsSerializer.getInstance().serialize(disabled)));
    }

    public void disable(ContextCommand<?> contextCommand) throws CommandException {
        List<ContextCommand<?>> disabled = getDisabledContextCommands();
        if (disabled.contains(contextCommand)) throw new CommandException("This command is already disabled.");
        disabled.add(contextCommand);
        Colossus.getDatabaseDriver().modifyTable(Colossus.getSelfUser(),
            table -> table.put(DISABLED_CONTEXT_COMMANDS_KEY, ContextCommandsSerializer.getInstance().serialize(disabled)));
    }

}
