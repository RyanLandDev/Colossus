package dev.ryanland.colossus.command.arguments.types.command;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.BasicCommand;
import dev.ryanland.colossus.command.Command;
import dev.ryanland.colossus.command.CommandType;
import dev.ryanland.colossus.command.ContextCommand;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.arguments.types.primitive.FutureArgumentStringResolver;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.events.command.CommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import dev.ryanland.colossus.sys.interactions.button.BaseButton;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class BasicCommandArgument extends FutureArgumentStringResolver<BasicCommand> {

    public static <T extends BasicCommand> ArgumentOptionData getAutocompleteChoiceData(List<T> commands) {
        List<Choice> choices = commands.stream()
            .map(command -> new Choice(command.getCommandType() == CommandType.CONTEXT_USER || command.getCommandType() == CommandType.CONTEXT_MESSAGE ?
                (command.getName() + " (" + command.getCommandType().getName() + ")") : command.getName(),
                command.getName())).toList();
        return new ArgumentOptionData(OptionType.STRING).addAutoCompletableChoices(choices);
    }

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return getAutocompleteChoiceData(CommandHandler.getAllCommands());
    }

    private record RichCommand(BasicCommand command, BaseButton button) {}

    @Override
    public CompletableFuture<BasicCommand> resolve(String arg, CommandEvent event) throws ArgumentException {
        CompletableFuture<BasicCommand> future = new CompletableFuture<>();
        long userId = event.getUser().getIdLong();

        // find commands
        Command cmd = CommandHandler.getCommand(arg);
        RichCommand command = new RichCommand(cmd,
            BaseButton.user(userId, Button.secondary("normal", "Command").withEmoji(Emoji.fromUnicode("#️⃣")),
                evt -> {
                    // set new reply callback, so newly sent reply methods in the command will work
                    if (event instanceof SlashCommandEvent) ((SlashCommandEvent) event).setRepliableEvent(evt);
                    future.complete(cmd);
                }));
        ContextCommand<User> userContextCmd = CommandHandler.getUserContextCommand(arg);
        RichCommand userContextCommand = new RichCommand(userContextCmd,
            BaseButton.user(userId, Button.secondary("user", "User").withEmoji(Emoji.fromUnicode("👤")),
                evt -> {
                if (event instanceof SlashCommandEvent) ((SlashCommandEvent) event).setRepliableEvent(evt);
                future.complete(userContextCmd);
                }));
        ContextCommand<Message> messageContextCmd = CommandHandler.getMessageContextCommand(arg);
        RichCommand messageContextCommand = new RichCommand(messageContextCmd,
            BaseButton.user(userId, Button.secondary("message", "Message").withEmoji(Emoji.fromUnicode("💬")),
                evt -> {
                if (event instanceof SlashCommandEvent) ((SlashCommandEvent) event).setRepliableEvent(evt);
                future.complete(messageContextCmd);
                }));

        // create results list
        List<RichCommand> results = Stream.of(command, userContextCommand, messageContextCommand)
            .filter(richCommand -> richCommand.command != null).toList();

        // send results
        if (results.isEmpty()) {
            throw new MalformedArgumentException("This command was not found.");
        }
        if (results.size() > 1) {
            event.reply(new PresetBuilder(Colossus.getDefaultPresetType())
                .setTitle("Multiple Results")
                .setDescription("There were multiple commands found with the name `" + arg + "`, please pick one type.")
                .addButtons(results.stream().map(RichCommand::button).toList())
            );
        }
        else {
            future.complete(results.get(0).command());
        }

        // return
        return future;
    }
}
