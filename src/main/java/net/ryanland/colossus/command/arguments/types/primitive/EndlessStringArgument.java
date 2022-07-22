package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashCommandEvent;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;

public class EndlessStringArgument extends Argument<String> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.STRING);
    }

    @Override
    public CompletableFuture<String> resolveSlashCommandArgument(Deque<OptionMapping> args, SlashCommandEvent event) throws ArgumentException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(args.pop().getAsString());
        return future;
    }

    @Override
    public CompletableFuture<String> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(String.join(" ", args));
        args.clear();
        return future;
    }
}
