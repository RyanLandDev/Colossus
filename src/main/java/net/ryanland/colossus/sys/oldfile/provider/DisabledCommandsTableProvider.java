package net.ryanland.colossus.sys.oldfile.provider;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DisabledCommandsTableProvider extends ListProvider<BasicCommand, Object> {

    @Override
    public String getKey() {
        return "disabled_commands";
    }

    @Override
    public List<BasicCommand> retrieve(@Nullable Object param) {
        return Colossus.getGlobalSupply().get(DisabledCommandHandler.DISABLED_COMMANDS_KEY, new ArrayList<>());
    }

    @Override
    public void update(List<BasicCommand> newValue) {
        Colossus.getGlobalSupply().put(DisabledCommandHandler.DISABLED_COMMANDS_KEY, newValue);
    }

    @Override
    public boolean contains(BasicCommand value) {
        return retrieve().contains(value);
    }

    @Override
    public void add(BasicCommand value) {
        List<BasicCommand> commands = retrieve();
        commands.add(value);
        update(commands);
    }

    @Override
    public void remove(BasicCommand value) {
        List<BasicCommand> commands = retrieve();
        commands.remove(value);
        update(commands);
    }
}
