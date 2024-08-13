package net.ryanland.colossus.events.repliable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback;
import net.dv8tion.jda.api.interactions.callbacks.IModalCallback;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.sys.snowflake.ColossusGuild;
import net.ryanland.colossus.sys.snowflake.ColossusMember;
import net.ryanland.colossus.sys.snowflake.ColossusUser;
import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.ryanland.colossus.sys.util.ExecutorUtil;

import java.util.Collections;
import java.util.List;

/**
 * This will edit the existing message instead of creating a new message, except for ephemeral replies to regular messages<br>
 */
public interface EditableRepliableEvent extends RepliableEvent {

    <E extends IReplyCallback & IMessageEditCallback> E getEvent();

    Message getMessage();

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

    @Override
    default void reply(String message, boolean ephemeral) {
        if (!getEvent().isAcknowledged()) {
            if (!ephemeral || (ephemeral && getMessage().isEphemeral())) {
                getEvent().editMessageEmbeds(Collections.emptyList())
                    .setComponents(Collections.emptyList())
                    .setContent(message)
                    .queue();
            } else {
                getEvent().reply(message)
                    .setEphemeral(true)
                    .queue();
            }
        } else {
            if (!ephemeral || (ephemeral && getMessage().isEphemeral())) {
                getEvent().getHook().editOriginalEmbeds(Collections.emptyList())
                    .setComponents(Collections.emptyList())
                    .setContent(message)
                    .queue();
            } else {
                getEvent().getHook().sendMessage(message)
                    .setEphemeral(true)
                    .queue();
            }
        }
    }

    @Override
    default void reply(MessageEmbed message, boolean ephemeral) {
        if (!getEvent().isAcknowledged()) {
            if (!ephemeral || (ephemeral && getMessage().isEphemeral())) {
                getEvent().editMessageEmbeds(message)
                    .setComponents(Collections.emptyList())
                    .setContent("")
                    .queue();
            } else {
                getEvent().replyEmbeds(message)
                    .setEphemeral(true)
                    .queue();
            }
        } else {
            if (!ephemeral || (ephemeral && getMessage().isEphemeral())) {
                getEvent().getHook().editOriginalEmbeds(message)
                    .setComponents(Collections.emptyList())
                    .setContent("")
                    .queue();
            } else {
                getEvent().getHook().sendMessageEmbeds(message)
                    .setEphemeral(true)
                    .queue();
            }
        }
    }

    @Override
    default void reply(PresetBuilder message) {
        List<ActionRow> actionRows = message.getActionRows();

        if (!message.isEphemeral() || (message.isEphemeral() && getMessage().isEphemeral())) {
            // remove old listeners
            if (!getMessage().getActionRows().isEmpty()) {
                ExecutorUtil.cancel(getMessage().getId(), false); // cancel an active action row emptier
                ButtonClickEvent.removeListeners(getMessage().getIdLong());
                SelectMenuEvent.removeListeners(getMessage().getIdLong());
            }
            // send reply
            // check if event is already acknowledged
            if (!getEvent().isAcknowledged()) {
                MessageEditCallbackAction reply = getEvent().editComponents(actionRows)
                    .setContent(message.getContent());
                if (!message.embedBuilder().isEmpty()) reply.setEmbeds(message.embed());
                else reply.setEmbeds();
                reply.queue(message::addComponentRowListeners);
            } else {
                WebhookMessageEditAction<Message> reply = getEvent().getHook().editOriginalComponents(actionRows)
                    .setContent(message.getContent());
                if (!message.embedBuilder().isEmpty()) reply.setEmbeds(message.embed());
                else reply.setEmbeds();
                reply.queue(message::addComponentRowListeners);
            }
        } else {
            if (!getEvent().isAcknowledged()) {
                ReplyCallbackAction reply = getEvent().replyComponents(actionRows)
                    .setContent(message.getContent())
                    .setEphemeral(true);
                if (!message.embedBuilder().isEmpty()) reply.setEmbeds(message.embed());
                else reply.setEmbeds();
                reply.queue(message::addComponentRowListeners);
            } else {
                WebhookMessageCreateAction<Message> reply = getEvent().getHook().sendMessageComponents(actionRows)
                    .setContent(message.getContent())
                    .setEphemeral(true);
                if (!message.embedBuilder().isEmpty()) reply.setEmbeds(message.embed());
                else reply.setEmbeds();
                reply.queue(message::addComponentRowListeners);
            }
        }
    }

    /**
     * Reply to this event with a {@link Modal}. Event must implement {@link IModalCallback} to work.<br>
     * Note: When overriding this method, do not forget to add a modal submit listener!
     *
     * @param modal
     * @param action
     * @see ModalSubmitEvent
     * @see ModalSubmitEvent#addListener(Long, String, CommandConsumer)
     */
    @Override
    default void reply(Modal modal, CommandConsumer<ModalSubmitEvent> action) {
        ((IModalCallback) getEvent()).replyModal(modal).queue();
        ModalSubmitEvent.addListener(getUser().getIdLong(), modal.getId(), action);
    }
}
