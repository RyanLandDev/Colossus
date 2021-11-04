package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class UserArgument extends SingleArgument<User> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.USER;
    }

    @Override
    public User resolveSlashCommandArgument(OptionMapping arg, SlashEvent event) throws ArgumentException {
        return arg.getAsUser();
    }

    @Override
    public User resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        return Colossus.getJda().getUserById(arg.replaceAll("\\D", ""));
    }
}
