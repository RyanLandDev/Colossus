package dev.ryanland.colossus.command;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.arguments.Argument;
import dev.ryanland.colossus.command.arguments.ArgumentSet;
import dev.ryanland.colossus.command.cooldown.CooldownManager;
import dev.ryanland.colossus.command.cooldown.MemoryCooldownManager;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.command.regular.CommandBuilder;
import dev.ryanland.colossus.command.regular.SubCommand;
import dev.ryanland.colossus.command.regular.SubCommandHolder;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public sealed abstract class Command extends BasicCommand permits BaseCommand {

    private List<SubCommand> subcommands;

    private CommandBuilder getInfo() {
        return getClass().getAnnotation(CommandBuilder.class);
    }

    public final Category getCategory() {
        try {
            return Colossus.getCategories().stream()
                .filter(category -> category.getName().equalsIgnoreCase(getInfo().category())).toList().get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("The category " + getInfo().category() + " does not exist.");
        }
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
        return getInfo().name();
    }

    public final String getDescription() {
        return getInfo().description();
    }

    @Override
    public final CommandType getCommandType() {
        return CommandType.REGULAR;
    }

    @Override
    public final boolean hasCooldown() {
        return getCooldown() != 0;
    }

    @Override
    public final float getCooldown() {
        return getInfo().cooldown();
    }

    @Override
    public final boolean isGuildOnly() {
        return getInfo().guildOnly();
    }

    @Override
    public final boolean canBeDisabled() {
        return getInfo().disabled();
    }

    @Override
    public CooldownManager getCooldownManager() {
        return MemoryCooldownManager.getInstance();
    }

    public final OptionData[] getOptionsData() {
        return (getArguments() == null ? new ArgumentSet() : getArguments()).values()
            .stream().map(Argument::getOptionData).toArray(OptionData[]::new);
    }

    @Override
    public final boolean isDisabled() {
        return getInfo().disabled();
    }

    public static Command of(String alias) {
        return CommandHandler.getCommand(alias);
    }

    public abstract ArgumentSet getArguments();

}
