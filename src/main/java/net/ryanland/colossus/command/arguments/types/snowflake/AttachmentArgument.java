package net.ryanland.colossus.command.arguments.types.snowflake;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MissingArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashCommandEvent;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class AttachmentArgument extends Argument<Message.Attachment> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.ATTACHMENT);
    }

    @Override
    public boolean ignoreMissingException() {
        return true;
    }

    @Override
    public CompletableFuture<Message.Attachment> resolveSlashCommandArgument(Deque<OptionMapping> args, SlashCommandEvent event) throws ArgumentException {
        CompletableFuture<Message.Attachment> future = new CompletableFuture<>();
        future.complete(args.pop().getAsAttachment());
        return future;
    }

    @Override
    public CompletableFuture<Message.Attachment> resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        // get attachments
        CompletableFuture<Message.Attachment> future = new CompletableFuture<>();
        List<Message.Attachment> attachments = event.getMessage().getAttachments();

        // get attachment position
        List<Argument<?>> arguments = event.getCommand().getArguments().values().stream()
            .filter(arg -> arg instanceof AttachmentArgument).toList();
        int pos = IntStream.range(0, arguments.size())
            .filter(i -> arguments.get(i).getName().equals(getName()))
            .findFirst().getAsInt();

        // check if missing
        if (pos+1 > attachments.size()) throw new MissingArgumentException();

        // return
        future.complete(attachments.get(pos));
        return future;
    }
}
