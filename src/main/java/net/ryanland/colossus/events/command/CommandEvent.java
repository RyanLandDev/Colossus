package net.ryanland.colossus.events.command;

import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.command.regular.SubCommandHolder;
import net.ryanland.colossus.sys.config.Config;

public abstract sealed class CommandEvent extends BasicCommandEvent permits SlashCommandEvent, MessageCommandEvent {

    @Override
    public abstract Command getCommand();

    public abstract SubCommandHolder getHeadSubCommandHolder();

    public abstract SubCommandHolder getNestedSubCommandHolder();

    public abstract void setCommand(Command command);

    public abstract void setHeadSubCommandHolder(SubCommandHolder subCommandHolder);

    public abstract void setNestedSubCommandHolder(SubCommandHolder subCommandHolder);

    public abstract ParsedArgumentMap getParsedArgs();

    public abstract void setParsedArgs(ParsedArgumentMap parsedArgs);

    public String getUsedPrefix() {
        if (this instanceof SlashCommandEvent) return "/";
        else return Config.getString("message_commands.prefix");
    }

    public abstract <T> T getArgument(String id);
}
