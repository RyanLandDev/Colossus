package net.ryanland.colossus.command.arguments.types.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.primitive.FutureArgumentStringResolver;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.events.command.SlashCommandEvent;
import net.ryanland.colossus.sys.interactions.button.BaseButton;
import net.ryanland.colossus.sys.message.PresetBuilder;

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
