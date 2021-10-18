package net.ryanland.colossus.events;

import net.dv8tion.jda.api.entities.*;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.arguments.parsing.ParsedArgumentMap;
import net.ryanland.colossus.sys.file.DatabaseDriver;
import net.ryanland.colossus.sys.file.Table;
import net.ryanland.colossus.sys.message.PresetBuilder;

public abstract class CommandEvent {

    public abstract Command getCommand();

    public abstract void setCommand(Command command);

    public abstract ParsedArgumentMap getParsedArgs();

    public abstract void setParsedArgs(ParsedArgumentMap parsedArgs);

    public abstract User getUser();

    public abstract Member getMember();

    public abstract Guild getGuild();

    /**
     * Get the {@link Table} of the user who executed this command.
     * <br>Note: This method may produce an error if the {@link DatabaseDriver} is not properly configured.
     * @see Table
     * @see DatabaseDriver
     */
    public Table<User> getUserTable() {
        return Colossus.getDatabaseDriver().get(getUser());
    }

    /**
     * Get the {@link Table} of the guild this command was executed in.
     * This will be {@code null} if the command was executed in DMs.
     * <br>Note: This method may produce an error if the {@link DatabaseDriver} is not properly configured.
     * @see Table
     * @see DatabaseDriver
     */
    public Table<Guild> getGuildTable() {
        if (getGuild() == null)
            return null;
        return Colossus.getDatabaseDriver().get(getGuild());
    }

    public abstract void reply(Message message);

    public abstract void reply(String message);

    public abstract void reply(MessageEmbed message);

    public abstract void reply(PresetBuilder message);

    public abstract <T> T getArgument(String id);
}
