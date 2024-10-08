package dev.ryanland.colossus.command.regular;

import dev.ryanland.colossus.ColossusBuilder;
import dev.ryanland.colossus.command.Command;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

/**
 * Interface for subcommands.<br>
 * <ul>
 * <li>Implement this interface in {@link Command} classes to make it a {@link SubCommand}
 * for an existing {@link SubCommandHolder}.</li>
 * <li>Don't register subcommands in your {@link ColossusBuilder}. This will register the subcommand as a normal command.
 * <br>Instead, reference your subcommand in your {@link SubCommandHolder}.</li>
 * <li>For nested subcommands (subcommand groups), implement both this and the {@link SubCommandHolder} interface.</li>
 * </ul>
 *
 * @see Command
 * @see SubCommandHolder
 */
public interface SubCommand {

    /**
     * Create a {@link SubcommandData} for this {@link SubCommand}
     */
    default SubcommandData getSlashData() {
        Command command = (Command) this;
        return new SubcommandData(command.getName(), command.getDescription()).addOptions(command.getOptionsData());
    }
}
