package net.ryanland.colossus.events.repliable;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.events.ButtonClickEvent;
import net.ryanland.colossus.events.ModalSubmitEvent;
import net.ryanland.colossus.events.SelectMenuEvent;
import net.ryanland.colossus.sys.entities.ColossusGuild;
import net.ryanland.colossus.sys.entities.ColossusMember;
import net.ryanland.colossus.sys.entities.ColossusUser;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.util.ExecutorUtil;

import java.util.Collections;
import java.util.List;

/**
 * This will edit the existing message instead of creating a new message, except for ephemeral messages<br>
 * Only for Button and Select Menu events
 */
public interface ComponentInteractionRepliableEvent extends RepliableEvent {

    GenericComponentInteractionCreateEvent getEvent();

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
        if (!ephemeral) {
            getEvent().editMessageEmbeds(Collections.emptyList())
                .setComponents(Collections.emptyList())
                .setContent(message)
                .queue();
        } else {
            getEvent().reply(message)
                .setEphemeral(true)
                .queue();
        }
    }

    @Override
    default void reply(MessageEmbed message, boolean ephemeral) {
        if (!ephemeral) {
            getEvent().editMessageEmbeds(message)
                .setComponents(Collections.emptyList())
                .setContent("")
                .queue();
        } else {
            getEvent().replyEmbeds(message)
                .setEphemeral(true)
                .queue();
        }
    }

    @Override
    default void reply(PresetBuilder message) {
        List<ActionRow> actionRows = message.getActionRows();

        if (!message.isEphemeral()) {
            // remove old listeners
            if (!getEvent().getMessage().getActionRows().isEmpty()) {
                ExecutorUtil.cancel(getEvent().getMessageId(), false); // cancel an active action row emptier
                ButtonClickEvent.removeListeners(getEvent().getMessageIdLong());
                SelectMenuEvent.removeListeners(getEvent().getMessageIdLong());
            }
            // send reply and set hook
            getEvent().editMessageEmbeds(message.embed())
                .setComponents(actionRows)
                .setContent(message.getContent())
                .queue(message::addComponentRowListeners);
        } else {
            // send reply and set hook
            getEvent().replyEmbeds(message.embed())
                .setComponents(actionRows)
                .setContent(message.getContent())
                .setEphemeral(true)
                .queue(message::addComponentRowListeners);
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
        getEvent().replyModal(modal).queue();
        ModalSubmitEvent.addListener(getUser().getIdLong(), modal.getId(), action);
    }
}
