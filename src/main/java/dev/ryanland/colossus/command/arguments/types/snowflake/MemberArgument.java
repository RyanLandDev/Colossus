package dev.ryanland.colossus.command.arguments.types.snowflake;

import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import dev.ryanland.colossus.sys.snowflake.ColossusMember;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class MemberArgument extends SnowflakeArgument<Member> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.USER);
    }

    @Override
    public ColossusMember resolveSlashCommandArg(OptionMapping arg, SlashCommandEvent event) throws ArgumentException {
        Member member = arg.getAsMember();
        if (member == null)
            throw new ArgumentException("The provided user must be a member of this server.");
        return new ColossusMember(member);
    }

    @Override
    public ColossusMember resolveMessageCommandArgument(MessageCommandEvent event, String id) throws ArgumentException {
        Member member = event.getGuild().getMemberById(id);
        if (member == null) throw new IllegalArgumentException();
        return new ColossusMember(member);
    }

}
