package net.ryanland.colossus.events;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.ColossusBuilder;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.sys.ExecutorUtil;
import net.ryanland.colossus.sys.entities.ColossusGuild;
import net.ryanland.colossus.sys.entities.ColossusMember;
import net.ryanland.colossus.sys.entities.ColossusUser;
import net.ryanland.colossus.sys.interactions.button.BaseButton;
import net.ryanland.colossus.sys.interactions.button.ButtonRow;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ClickButtonEvent implements RepliableEvent {

    private static final HashMap<Long, List<ButtonIdentifier>> MESSAGE_BUTTONS = new HashMap<>();
    private static final HashMap<ButtonIdentifier, BaseButton> BUTTONS = new HashMap<>();

    /**
     * Adds a button listener that will listen indefinitely
     * @param msgId The ID of the message where the buttons are located
     * @param buttonRows The list of {@link ButtonRow}s to listen for
     * @see #addListener(Long, List, Runnable, long, TimeUnit)
     * @see #addListener(Long, List, Runnable)
     */
    public static void addListener(Long msgId, List<ButtonRow> buttonRows) {
        List<ButtonIdentifier> identifiers = new ArrayList<>();
        buttonRows.forEach(buttonRow -> buttonRow.getButtons().forEach(button -> {
            ButtonIdentifier buttonIdentifier = new ButtonIdentifier(msgId, button.button().getId());
            BUTTONS.put(buttonIdentifier, button);
            identifiers.add(buttonIdentifier);
        }));
        MESSAGE_BUTTONS.put(msgId, identifiers);
    }

    /**
     * Adds a button listener that will stop listening after the provided time period
     * @param msgId The ID of the message where the buttons are located
     * @param buttonRows The list of {@link ButtonRow}s to listen for
     * @param actionRowEmptier A {@link Runnable} which removes all action rows from the message
     * @param timeAmount The amount of time to listen for
     * @param timeUnit The time unit used
     * @see #addListener(Long, List)
     * @see #addListener(Long, List, Runnable)
     */
    public static void addListener(Long msgId, List<ButtonRow> buttonRows,
                                   Runnable actionRowEmptier, long timeAmount, TimeUnit timeUnit) {
        addListener(msgId, buttonRows);
        ExecutorUtil.schedule(msgId.toString(), () -> {
            actionRowEmptier.run();
            removeListeners(msgId);
        }, timeAmount, timeUnit);
    }

    /**
     * Adds a button listener that will stop listening after the time period defined in {@link Colossus}
     * @param msgId The ID of the message where the buttons are located
     * @param buttonRows The list of {@link ButtonRow}s to listen for
     * @param actionRowEmptier A {@link Runnable} which removes all action rows from the message
     * @see ColossusBuilder#setDefaultButtonListenerExpirationTime(long, TimeUnit)
     * @see #addListener(Long, List)
     * @see #addListener(Long, List, Runnable, long, TimeUnit)
     */
    public static void addListener(Long msgId, List<ButtonRow> buttonRows, Runnable actionRowEmptier) {
        addListener(msgId, buttonRows, actionRowEmptier,
            Colossus.getDefaultButtonListenerExpirationTimeAmount(), Colossus.getDefaultButtonListenerExpirationTimeUnit());
    }

    /**
     * Removes all listeners associated with the provided message
     * @param msgId The ID of the message where the buttons are located
     * @see #addListener(Long, List)
     * @see #addListener(Long, List, Runnable, long, TimeUnit)
     * @see #addListener(Long, List, Runnable)
     */
    public static void removeListeners(Long msgId) {
        MESSAGE_BUTTONS.remove(msgId).forEach(BUTTONS::remove);
    }

    public final ButtonInteractionEvent event;
    public final BaseButton button;

    public ClickButtonEvent(ButtonInteractionEvent event) {
        this.event = event;
        this.button = BUTTONS.get(new ButtonIdentifier(event.getMessageIdLong(), event.getButton().getId()));
    }

    /**
     * Handle this event; execute it
     */
    public void handle() throws CommandException {
        if (button == null) return;
        if (button.onClick() != null) button.onClick().consume(this);
    }

    public ButtonInteractionEvent getEvent() {
        return event;
    }

    public BaseButton getButton() {
        return button;
    }

    @Override
    public ColossusUser getUser() {
        return new ColossusUser(event.getUser());
    }

    @Override
    public ColossusMember getMember() {
        return new ColossusMember(event.getMember());
    }

    @Override
    public ColossusGuild getGuild() {
        return new ColossusGuild(event.getGuild());
    }

    // reply methods ----- for button clicks, this will edit the existing message instead of creating a new message,
    //                     except for ephemeral messages

    @Override
    public void reply(Message message, boolean ephemeral) {
        if (!ephemeral) {
            event.editMessage(message).queue();
        } else {
            event.reply(message)
                .setEphemeral(true)
                .queue();
        }
    }

    @Override
    public void reply(String message, boolean ephemeral) {
        if (!ephemeral) {
            event.editMessageEmbeds(Collections.emptyList())
                .setActionRows(Collections.emptyList())
                .setContent(message)
                .queue();
        } else {
            event.reply(message)
                .setEphemeral(true)
                .queue();
        }
    }

    @Override
    public void reply(MessageEmbed message, boolean ephemeral) {
        if (!ephemeral) {
            event.editMessageEmbeds(message)
                .setActionRows(Collections.emptyList())
                .setContent("")
                .queue();
        } else {
            event.replyEmbeds(message)
                .setEphemeral(true)
                .queue();
        }
    }

    @Override
    public void reply(PresetBuilder message) {
        InteractionHook hook;
        List<ActionRow> actionRows = message.getButtonRows().stream().map(ButtonRow::toActionRow).collect(Collectors.toList());

        if (!message.isEphemeral()) {
            // remove old button listeners
            if (!event.getMessage().getActionRows().isEmpty()) {
                ExecutorUtil.cancel(event.getMessageId(), false); // cancel an active action row emptier
                removeListeners(event.getMessageIdLong());
            }
            // send reply and set hook
            hook = event.editMessageEmbeds(message.embed())
                .setActionRows(actionRows)
                .setContent(message.getContent())
                .complete();

        } else {
            // send reply and set hook
            hook = event.replyEmbeds(message.embed())
                .addActionRows(actionRows)
                .setContent(message.getContent())
                .setEphemeral(true)
                .complete();
        }
        // add listener for the buttons
        if (!message.getButtonRows().isEmpty()) {
            ClickButtonEvent.addListener(
                event.getMessageIdLong(), message.getButtonRows(),
                () -> hook.editOriginalComponents(Collections.emptyList()).queue()
            );
        }
    }

    private record ButtonIdentifier(Long msgId, String buttonId) {}
}
