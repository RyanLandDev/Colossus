package dev.ryanland.colossus.events;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import dev.ryanland.colossus.events.command.ContextCommandEvent;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import dev.ryanland.colossus.events.repliable.ButtonClickEvent;
import dev.ryanland.colossus.events.repliable.ModalSubmitEvent;
import dev.ryanland.colossus.events.repliable.SelectMenuEvent;
import dev.ryanland.colossus.sys.config.Config;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class InternalEventListener extends ListenerAdapter {

    // Execute slash command
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!Config.getBoolean("slash_commands.enabled")) return;
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
            if (!Config.getBoolean("message_commands.enabled") ||
                Config.getString("message_commands.prefix") == null) return;
            new Thread(() -> CommandHandler.run(new MessageCommandEvent(event))).start();
        }
    }

    private static final Map<String, CommandConsumer<ButtonClickEvent>> STATIC_BUTTON_LISTENERS = new HashMap<>();
    private static final Map<String, CommandConsumer<ButtonClickEvent>> STATIC_STARTSWITH_BUTTON_LISTENERS = new HashMap<>();

    public static void registerStaticButtonListener(String buttonId, CommandConsumer<ButtonClickEvent> listener) {
        STATIC_BUTTON_LISTENERS.put(buttonId, listener);
    }

    public static void registerStaticStartsWithButtonListener(String buttonId, CommandConsumer<ButtonClickEvent> listener) {
        STATIC_STARTSWITH_BUTTON_LISTENERS.put(buttonId, listener);
    }

    // Click button
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        try {
            if (STATIC_BUTTON_LISTENERS.containsKey(buttonId)) {
                STATIC_BUTTON_LISTENERS.get(buttonId).accept(new ButtonClickEvent(event));
            } else {
                for (Entry<String, CommandConsumer<ButtonClickEvent>> entries : STATIC_STARTSWITH_BUTTON_LISTENERS.entrySet()) {
                    if (buttonId.startsWith(entries.getKey())) {
                        entries.getValue().accept(new ButtonClickEvent(event));
                        return;
                    }
                }

                new ButtonClickEvent(event).handle();
            }
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

    // Submit string select menu
    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        onSelectInteraction(event);
    }

    // Submit entity select menu
    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        onSelectInteraction(event);
    }

    private void onSelectInteraction(GenericSelectMenuInteractionEvent<?, ?> event) {
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
