package net.ryanland.colossus.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SubCommandHolder;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.Table;

public abstract class CommandEvent implements RepliableEvent {

    public abstract Command getCommand();

    public abstract SubCommandHolder getHeadSubCommandHolder();

    public abstract SubCommandHolder getNestedSubCommandHolder();

    public abstract void setCommand(Command command);

    public abstract void setHeadSubCommandHolder(SubCommandHolder subCommandHolder);

    public abstract void setNestedSubCommandHolder(SubCommandHolder subCommandHolder);

    public abstract String getName();

    public abstract ParsedArgumentMap getParsedArgs();

    public abstract void setParsedArgs(ParsedArgumentMap parsedArgs);

    public abstract MessageChannel getChannel();

    public abstract boolean isFromGuild();

    public abstract JDA getJDA();

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

    public String getUsedPrefix() {
        if (this instanceof SlashEvent) return "/";
        else return getGuildPrefix();
    }

    public String getGuildPrefix() {
        String prefix = getGuildTable().get("_prf");
        if (prefix != null)
            return prefix;
        else
            return Colossus.getConfig().getPrefix();
    }

    public abstract <T> T getArgument(String id);
}
