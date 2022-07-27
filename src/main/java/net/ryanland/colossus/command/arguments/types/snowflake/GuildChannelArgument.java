package net.ryanland.colossus.command.arguments.types.snowflake;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.command.MessageCommandEvent;
import net.ryanland.colossus.events.command.SlashCommandEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuildChannelArgument extends SnowflakeArgument<GuildChannel> {

    private final ChannelType[] permittedChannelTypes;

    /**
     * Creates a ChannelArgument
     * @param permittedChannelTypes The channel types permitted by this argument.
     *                              If none are provided, all types are allowed.
     */
    public GuildChannelArgument(ChannelType... permittedChannelTypes) {
        this.permittedChannelTypes = permittedChannelTypes;
    }

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return (ArgumentOptionData) new ArgumentOptionData(OptionType.CHANNEL).setChannelTypes(permittedChannelTypes);
    }

    @Override
    public GuildChannel resolveSlashCommandArgument(OptionMapping arg, SlashCommandEvent event) throws ArgumentException {
        return arg.getAsChannel();
    }

    @Override
    public GuildChannel resolveMessageCommandArgument(MessageCommandEvent event, String id) throws ArgumentException {
        return checkIfChannelTypeIsPermitted(Colossus.getJDA().getGuildChannelById(id));
    }

    private GuildChannel checkIfChannelTypeIsPermitted(GuildChannel channel) throws ArgumentException {
        if (permittedChannelTypes.length == 0 || List.of(permittedChannelTypes).contains(channel.getType()))
            return channel;
        else
            throw new MalformedArgumentException("Only channels of the following types are permitted: " +
                Arrays.stream(permittedChannelTypes)
                    .map(type -> type.name().charAt(0) + type.name().substring(1).toLowerCase())
                    .collect(Collectors.joining(", ")));
    }

}
