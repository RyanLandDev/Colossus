package net.ryanland.colossus.events;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.events.command.ContextCommandEvent;
import net.ryanland.colossus.events.command.MessageCommandEvent;
import net.ryanland.colossus.events.command.SlashCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;
import org.jetbrains.annotations.NotNull;

public class InternalEventListener extends ListenerAdapter {

    // Execute slash command
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        new Thread(() -> CommandHandler.run(new SlashCommandEvent(event))).start();
    }

    // Slash command autocomplete
    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        CommandHandler.handleAutocompleteEvent(event);
    }

    // Execute message command
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            new Thread(() -> CommandHandler.run(new MessageCommandEvent(event))).start();
        }
    }

    // Click button
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        try {
            new ButtonClickEvent(event).handle();
        } catch (CommandException e) {
            event.deferReply().addEmbeds(
                new PresetBuilder(Colossus.getErrorPresetType(), e.getMessage()).embed()
            ).queue();
        }
    }

    // Submit modal
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        try {
            new ModalSubmitEvent(event).handle();
        } catch (CommandException e) {
            event.deferReply().addEmbeds(
                new PresetBuilder(Colossus.getErrorPresetType(), e.getMessage()).embed()
            ).queue();
        }
    }

    // Submit select menu
    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        try {
            new SelectMenuEvent(event).handle();
        } catch (CommandException e) {
            event.deferReply().addEmbeds(
                new PresetBuilder(Colossus.getErrorPresetType(), e.getMessage()).embed()
            ).queue();
        }
    }

    // Context commands
    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        onContextInteraction(event);
    }

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {
        onContextInteraction(event);
    }

    private <T extends ISnowflake> void onContextInteraction(GenericContextInteractionEvent<T> event) {
        CommandHandler.run(new ContextCommandEvent<>(event));
    }
}
