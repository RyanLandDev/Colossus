package net.ryanland.colossus.command.impl;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.ryanland.colossus.command.BaseCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.SlashCommand;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.snowflake.GuildChannelArgument;
import net.ryanland.colossus.events.SlashCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

@CommandBuilder(
        name = "deletechannel",
        description = "Deletes a channel"
)
public class DeleteChannelCommand extends BaseCommand implements SlashCommand {

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArguments(
            new GuildChannelArgument()
                .id("channel")
                .description("The channel to delete")
        );
    }

    @Override
    public void run(SlashCommandEvent event) throws CommandException {
        GuildChannel channel = event.getArgument("channel");
        channel.delete().queue();

        PresetBuilder message = new PresetBuilder()
            .setTitle("Channel Deleted")
            .setDescription("The channel #" + channel.getName() + " has been successfully deleted.");
        event.reply(message);
    }
}
