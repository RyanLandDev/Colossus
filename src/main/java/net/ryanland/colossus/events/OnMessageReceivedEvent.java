package net.ryanland.colossus.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.command.executor.CommandHandler;
import org.jetbrains.annotations.NotNull;

public class OnMessageReceivedEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        CommandHandler.run(new MessageCommandEvent(event));
    }
}