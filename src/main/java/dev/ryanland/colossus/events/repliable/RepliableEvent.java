package dev.ryanland.colossus.events.repliable;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import dev.ryanland.colossus.sys.database.entities.GuildEntity;
import dev.ryanland.colossus.sys.database.entities.MemberEntity;
import dev.ryanland.colossus.sys.database.entities.UserEntity;
import dev.ryanland.colossus.sys.interactions.menu.InteractionMenu;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import dev.ryanland.colossus.sys.snowflake.ColossusGuild;
import dev.ryanland.colossus.sys.snowflake.ColossusMember;
import dev.ryanland.colossus.sys.snowflake.ColossusUser;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Interface for events that were fired following the action of a user and have the ability to be replied to using a message.
 */
public interface RepliableEvent {

    ColossusUser getUser();

    ColossusMember getMember();

    ColossusGuild getGuild();

    Channel getChannel();

    default <R extends UserEntity> R getUserEntity() {
        return getUser().getEntity();
    }

    default <R extends MemberEntity> R getMemberEntity() {
        return getMember().getEntity();
    }

    default <R extends GuildEntity> R getGuildEntity() {
        return getGuild().getEntity();
    }

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
     * Note: When overriding this method, do not forget to add component row listeners!
     * @see PresetBuilder#setEphemeral(boolean)
     * @see ButtonClickEvent#addListener(Long, List)
     * @see ButtonClickEvent#addListener(Long, List, Runnable)
     * @see ButtonClickEvent#addListener(Long, List, Runnable, long, TimeUnit)
     */
    void reply(PresetBuilder message);

    /**
     * Reply to this event with a {@link PresetBuilder} using the configured success preset type, with a custom description<br>
     * @param description The description of the {@link PresetBuilder}
     */
    default void replySuccess(String description) {
        reply(new PresetBuilder(Colossus.getSuccessPresetType(), description));
    }

    /**
     * Reply to this event with a {@link PresetBuilder} using the configured success preset type, with a custom title and description<br>
     * @param title The title of the {@link PresetBuilder}
     * @param description The description of the {@link PresetBuilder}
     */
    default void replySuccess(String title, String description) {
        reply(new PresetBuilder(Colossus.getSuccessPresetType(), title, description));
    }

    /**
     * Reply to this event with a {@link PresetBuilder} using the configured error preset type, with a custom description<br>
     * @param description The description of the {@link PresetBuilder}
     */
    default void replyError(String description) {
        reply(new PresetBuilder(Colossus.getErrorPresetType(), description));
    }

    /**
     * Reply to this event with a {@link PresetBuilder} using the configured error preset type, with a custom title and description<br>
     * @param title The title of the {@link PresetBuilder}
     * @param description The description of the {@link PresetBuilder}
     */
    default void replyError(String title, String description) {
        reply(new PresetBuilder(Colossus.getErrorPresetType(), title, description));
    }

    /**
     * Reply to this event with a {@link Modal}<br>
     * Note: When overriding this method, do not forget to add a modal submit listener!
     * @see ModalSubmitEvent
     * @see ModalSubmitEvent#addListener(Long, String, CommandConsumer)
     */
    void reply(Modal modal, CommandConsumer<ModalSubmitEvent> action);

    /**
     * Reply to this event with a {@link InteractionMenu}
     */
    default void reply(InteractionMenu menu) throws CommandException {
        menu.send(this);
    }

}
