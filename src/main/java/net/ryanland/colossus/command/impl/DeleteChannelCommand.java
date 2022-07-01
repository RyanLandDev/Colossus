package net.ryanland.colossus.command.impl;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.ryanland.colossus.command.*;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.snowflake.GuildChannelArgument;
import net.ryanland.colossus.command.permissions.BotOwnerRequirement;
import net.ryanland.colossus.command.permissions.PermissionBuilder;
import net.ryanland.colossus.command.permissions.PermissionHolder;
import net.ryanland.colossus.events.SlashEvent;
import net.ryanland.colossus.sys.interactions.button.BaseButton;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.List;

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
    public void run(SlashEvent event) throws CommandException {
        GuildChannel channel = event.getArgument("channel");
        channel.delete().queue();

        PresetBuilder message = new PresetBuilder()
            .setTitle("Channel Deleted")
            .setDescription("The channel #" + channel.getName() + " has been successfully deleted.");
        event.reply(message);
    }
}
