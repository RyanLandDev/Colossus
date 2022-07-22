package net.ryanland.colossus.command.executor;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.sys.file.serializer.BasicCommandsSerializer;

import java.util.ArrayList;
import java.util.List;

public class DisabledCommandHandler {

    private static final DisabledCommandHandler INSTANCE = new DisabledCommandHandler();
    private static final String DISABLED_COMMANDS_KEY = "_dc";

    public static DisabledCommandHandler getInstance() {
        return INSTANCE;
    }

    public List<BasicCommand> getDisabledCommands() {
        return BasicCommandsSerializer.getInstance().deserialize(Colossus.getGlobalTable().get(DISABLED_COMMANDS_KEY, new ArrayList<>()));
    }

    public boolean isDisabled(BasicCommand command) {
        return getDisabledCommands().contains(command);
    }

    public void enable(BasicCommand command) throws CommandException {
        List<BasicCommand> disabled = getDisabledCommands();
        if (!disabled.contains(command)) throw new CommandException("This command is already enabled.");
        disabled.remove(command);
        Colossus.getDatabaseDriver().modifyTable(Colossus.getSelfUser(),
            table -> table.put(DISABLED_COMMANDS_KEY, BasicCommandsSerializer.getInstance().serialize(disabled)));
    }

    public void disable(BasicCommand command) throws CommandException {
        List<BasicCommand> disabled = getDisabledCommands();
        if (disabled.contains(command)) throw new CommandException("This command is already disabled.");
        if (!command.canBeDisabled()) throw new CommandException("This command cannot be disabled.");
        disabled.add(command);
        Colossus.getDatabaseDriver().modifyTable(Colossus.getSelfUser(),
            table -> table.put(DISABLED_COMMANDS_KEY, BasicCommandsSerializer.getInstance().serialize(disabled)));
    }

}
