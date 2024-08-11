package net.ryanland.colossus.events.repliable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.callbacks.IModalCallback;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.sys.entities.ColossusGuild;
import net.ryanland.colossus.sys.entities.ColossusMember;
import net.ryanland.colossus.sys.entities.ColossusUser;
import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface InteractionRepliableEvent extends RepliableEvent {

    IReplyCallback getEvent();

    @Override
    default ColossusUser getUser() {
        return new ColossusUser(getEvent().getUser());
    }

    @Override
    default ColossusMember getMember() {
        return new ColossusMember(getEvent().getMember());
    }

    @Override
    default ColossusGuild getGuild() {
        return new ColossusGuild(getEvent().getGuild());
    }

    /**
     * Reply to this event with a plain text message
     *
     * @param message
     * @param ephemeral If true, make this reply ephemeral when possible
     */
    @Override
    default void reply(String message, boolean ephemeral) {
        if (!getEvent().isAcknowledged()) {
            getEvent().reply(message).setEphemeral(ephemeral).queue();
        } else {
            getEvent().getHook().sendMessage(message).setEphemeral(ephemeral).queue();
        }
    }

    /**
     * Reply to this event with a {@link MessageEmbed}
     *
     * @param message
     * @param ephemeral If true, make this reply ephemeral when possible
     */
    @Override
    default void reply(MessageEmbed message, boolean ephemeral) {
        if (!getEvent().isAcknowledged()) {
            getEvent().replyEmbeds(message).setEphemeral(ephemeral).queue();
        } else {
            getEvent().getHook().sendMessageEmbeds(message).setEphemeral(ephemeral).queue();
        }
    }

    /**
     * Reply to this event with a {@link PresetBuilder}<br>
     * Note: When overriding this method, do not forget to add a button listener!
     *
     * @param message
     * @see PresetBuilder#setEphemeral(boolean)
     * @see ButtonClickEvent#addListener(Long, List)
     * @see ButtonClickEvent#addListener(Long, List, Runnable)
     * @see ButtonClickEvent#addListener(Long, List, Runnable, long, TimeUnit)
     */
    @Override
    default void reply(PresetBuilder message) {
        if (!getEvent().isAcknowledged()) {
            ReplyCallbackAction reply = getEvent().replyComponents(message.getActionRows())
                .setEphemeral(message.isEphemeral())
                .setContent(message.getContent());
            if (!message.embedBuilder().isEmpty()) reply.setEmbeds(message.embed());
            else reply.setEmbeds();
            reply.queue(message::addComponentRowListeners);
        } else {
            WebhookMessageCreateAction<Message> reply = getEvent().getHook().sendMessageComponents(message.getActionRows())
                .setEphemeral(message.isEphemeral())
                .setContent(message.getContent());
            if (!message.embedBuilder().isEmpty()) reply.setEmbeds(message.embed());
            else reply.setEmbeds();
            reply.queue(message::addComponentRowListeners);
        }
    }

    /**
     * Reply to this event with a {@link Modal}<br>
     * Note: When overriding this method, do not forget to add a modal submit listener!
     *
     * @param modal
     * @param action
     * @see ModalSubmitEvent
     * @see ModalSubmitEvent#addListener(Long, String, CommandConsumer)
     */
    @Override
    default void reply(Modal modal, CommandConsumer<ModalSubmitEvent> action) {
        try {
            ((IModalCallback) getEvent()).replyModal(modal).queue();
        } catch (ClassCastException e) {
            Colossus.LOGGER.error(getClass().getName() + " - This event does not support replying with modals");
            e.printStackTrace();
        }
        ModalSubmitEvent.addListener(getUser().getIdLong(), modal.getId(), action);
    }
}
