package dev.ryanland.colossus.command.arguments.types.command;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.arguments.types.primitive.FutureArgumentStringResolver;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.sys.interactions.button.BaseButton;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.ContextCommand;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.events.command.CommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;

import java.util.concurrent.CompletableFuture;

public class ContextCommandArgument extends FutureArgumentStringResolver<ContextCommand<?>> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return BasicCommandArgument.getAutocompleteChoiceData(CommandHandler.getContextCommands());
    }

    @Override
    public CompletableFuture<ContextCommand<?>> resolve(String arg, CommandEvent event) throws ArgumentException {
        CompletableFuture<ContextCommand<?>> future = new CompletableFuture<>();

        ContextCommand<User> userCommand = CommandHandler.getUserContextCommand(arg);
        ContextCommand<Message> messageCommand = CommandHandler.getMessageContextCommand(arg);

        // no results
        if (userCommand == null && messageCommand == null) {
            throw new MalformedArgumentException("This command was not found.");
        }
        // user results
        if (userCommand != null && messageCommand == null) {
            future.complete(userCommand);
            return future;
        }
        // message results
        if (userCommand == null) {
            future.complete(messageCommand);
            return future;
        }
        // both results
        event.reply(new PresetBuilder(Colossus.getDefaultPresetType())
            .setTitle("Multiple Results")
            .setDescription("There was both a user and message context command found with the name `" + arg + "`, please pick one type.")
            .addButtons(BaseButton.user(event.getUser().getIdLong(),
                Button.secondary("user", "User").withEmoji(Emoji.fromUnicode("👤")),
                evt -> {
                    // set new reply callback, so newly sent reply methods in the command will work
                    if (event instanceof SlashCommandEvent) ((SlashCommandEvent) event).setRepliableEvent(evt);
                    future.complete(userCommand);
                }
            ), BaseButton.user(event.getUser().getIdLong(),
                Button.secondary("message", "Message").withEmoji(Emoji.fromUnicode("💬")),
                evt -> {
                    if (event instanceof SlashCommandEvent) ((SlashCommandEvent) event).setRepliableEvent(evt);
                    future.complete(messageCommand);
                }
            ))
        );
        return future;
    }
}
