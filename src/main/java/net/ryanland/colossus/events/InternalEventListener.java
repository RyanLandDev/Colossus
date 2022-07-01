package net.ryanland.colossus.events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.sys.message.PresetBuilder;
import org.jetbrains.annotations.NotNull;

public class InternalEventListener extends ListenerAdapter {

    // Execute slash command
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandHandler.run(new SlashCommandEvent(event));
    }

    // Execute message command
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getAuthor().isBot())
            CommandHandler.run(new MessageCommandEvent(event));
    }

    // Click button
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        try {
            new ClickButtonEvent(event).handle();
        } catch (CommandException e) {
            event.deferReply().addEmbeds(
                new PresetBuilder(Colossus.getErrorPresetType(), e.getMessage()).embed()
            ).queue();
        }
    }
}
