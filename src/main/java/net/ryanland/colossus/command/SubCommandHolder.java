package net.ryanland.colossus.command;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.ryanland.colossus.ColossusBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface for commands holding subcommands.
 * <br><br>
 * <ul>
 * <li>Implement this interface in {@link Command} classes to transform the command into a command with {@link SubCommand}s.</li>
 * <li>Don't implement either of the {@link MessageCommand}/{@link SlashCommand}/{@link CombinedCommand} interfaces as well.
 * <br>This will have no effect.</li>
 * <li>The {@link Command#getArguments()} method will be ignored.</li>
 * <li>Define your subcommands in the {@link #getSubCommands()} method.</li>
 * <li>For nested subcommands (subcommand groups), implement both this and the {@link SubCommand} interface in a subcommand.</li>
 * <li>As for every command, do not forget to register it in your {@link ColossusBuilder}.
 * <br>Subcommands and nested subcommand holders should not be registered.</li>
 * </ul>
 *
 * @see Command
 * @see SubCommand
 */
public interface SubCommandHolder {

    List<SubCommand> getSubCommands();

    /**
     * Returns a list of all subcommands and nested subcommands, excluding the holders
     */
    default List<SubCommand> getRealSubCommands() {
        List<SubCommand> subcommands = new ArrayList<>();
        for (SubCommand subcommand : getSubCommands()) {
            if (subcommand instanceof SubCommandHolder)
                subcommands.addAll(((SubCommandHolder) subcommand).getSubCommands());
            else
                subcommands.add(subcommand);
        }
        return subcommands;
    }

    /**
     * Create a {@link SubcommandGroupData} for this {@link SubCommandHolder}
     */
    default SubcommandGroupData getSlashCommandData() {
        Command command = (Command) this;
        return new SubcommandGroupData(command.getName(), command.getDescription())
            .addSubcommands(getSubCommands().stream()
                .filter(subcommand -> subcommand instanceof SlashCommand)
                .map(SubCommand::getSlashData)
                .collect(Collectors.toList()));
    }

    /**
     * Returns a formatted list of subcommands, in the form of "`a` `b` `c`" etc.
     */
    default String getFormattedSubCommands() {
        return "`" + getSubCommands().stream()
            .map(subcommand -> ((Command) subcommand).getName()).collect(Collectors.joining("` `")) + "`";
    }

}
