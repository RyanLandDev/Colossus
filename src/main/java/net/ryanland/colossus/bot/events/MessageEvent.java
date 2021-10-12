package net.ryanland.colossus.bot.events;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.sys.message.acceptors.MessageAcceptor;

public class MessageEvent extends ListenerAdapter {

    MessageAcceptor[] acceptors = new MessageAcceptor[]{

    };

    /*
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        for (MessageAcceptor acceptor : acceptors) {
            if (acceptor.check(event.getMessage())) {
                break;
            }
        }
    }
     */

}
