package net.ryanland.colossus.events;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.sys.entities.ColossusGuild;
import net.ryanland.colossus.sys.entities.ColossusMember;
import net.ryanland.colossus.sys.entities.ColossusUser;
import net.ryanland.colossus.sys.interactions.menu.InteractionMenu;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Interface for events that were fired following the action of a user and have the ability to be replied to using a message.
 */
public interface RepliableEvent {

    ColossusUser getUser();

    ColossusMember getMember();

    ColossusGuild getGuild();

    /**
     * Reply to this event with a {@link Message} object
     */
    default void reply(Message message) {
        reply(message, false);
    }

    /**
     * Reply to this event with a {@link Message} object
     * @param ephemeral If true, make this reply ephemeral when possible
     */
    void reply(Message message, boolean ephemeral);

    /**
     * Reply to this event with a plain text message
     */
    default void reply(String message) {
        reply(message, false);
    }

    /**
     * Reply to this event with a plain text message
     * @param ephemeral If true, make this reply ephemeral when possible
     */
    void reply(String message, boolean ephemeral);

    /**
     * Reply to this event with a {@link MessageEmbed}
     */
    default void reply(MessageEmbed message) {
        reply(message, false);
    }

    /**
     * Reply to this event with a {@link MessageEmbed}
     * @param ephemeral If true, make this reply ephemeral when possible
     */
    void reply(MessageEmbed message, boolean ephemeral);

    /**
     * Reply to this event with a {@link PresetBuilder}<br>
     * Note: When overriding this method, do not forget to add a button listener!
     * @see PresetBuilder#setEphemeral(boolean)
     * @see ClickButtonEvent#addListener(Long, List)
     * @see ClickButtonEvent#addListener(Long, List, Runnable)
     * @see ClickButtonEvent#addListener(Long, List, Runnable, long, TimeUnit)
     */
    void reply(PresetBuilder message);

    /**
     * Reply to this event with a {@link InteractionMenu}
     */
    default void reply(InteractionMenu menu) throws CommandException {
        menu.send(this);
    }

}
