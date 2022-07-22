package net.ryanland.colossus.command;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.cooldown.CooldownManager;
import net.ryanland.colossus.command.cooldown.MemoryCooldownManager;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;

import java.util.List;

import static net.ryanland.colossus.command.info.HelpMaker.getInfo;

public sealed abstract class Command extends BasicCommand permits BaseCommand {

    private Category category;
    private List<SubCommand> subcommands;

    public final Command setCategory(Category category) {
        this.category = category;
        return this;
    }

    public final Category getCategory() {
        return category;
    }

    public final List<SubCommand> getSubCommands() {
        if (!(this instanceof SubCommandHolder))
            throw new IllegalStateException("This Command is not an instance of SubCommandHolder");
        if (subcommands == null)
            subcommands = ((SubCommandHolder) this).registerSubCommands();
        return subcommands;
    }

    // CommandData getters --------------------

    @Override
    public final String getName() {
        return getInfo(this).name();
    }

    public final String getDescription() {
        return getInfo(this).description();
    }

    @Override
    public final CommandType getCommandType() {
        return CommandType.NORMAL;
    }

    @Override
    public final boolean hasCooldown() {
        return getCooldown() != 0;
    }

    @Override
    public final int getCooldown() {
        return getInfo(this).cooldown();
    }

    public final boolean isGuildOnly() {
        return getInfo(this).guildOnly();
    }

    @Override
    public CooldownManager getCooldownManager() {
        return MemoryCooldownManager.getInstance();
    }

    public final OptionData[] getOptionsData() {
        return (getArguments() == null ? new ArgumentSet() : getArguments())
            .stream().map(Argument::getOptionData).toArray(OptionData[]::new);
    }

    @Override
    public final boolean isDisabled() {
        return DisabledCommandHandler.getInstance().isDisabled(this);
    }

    public static Command of(String alias) {
        return CommandHandler.getCommand(alias);
    }

    public abstract ArgumentSet getArguments();

}
