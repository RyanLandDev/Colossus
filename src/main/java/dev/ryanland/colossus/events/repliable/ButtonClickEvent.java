package dev.ryanland.colossus.events.repliable;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.ColossusBuilder;
import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.sys.interactions.button.BaseButton;
import dev.ryanland.colossus.sys.interactions.button.ButtonRow;
import dev.ryanland.colossus.sys.util.ExecutorUtil;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class ButtonClickEvent implements EditableRepliableEvent {

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
        List<ButtonIdentifier> identifiers = MESSAGE_BUTTONS.getOrDefault(msgId, new ArrayList<>());
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
            try {
                actionRowEmptier.run();
            } catch (Exception ignored) {}
            removeListeners(msgId);
        }, timeAmount, timeUnit);
    }

    /**
     * Adds a button listener that will stop listening after the time period defined in {@link Colossus}
     * @param msgId The ID of the message where the buttons are located
     * @param buttonRows The list of {@link ButtonRow}s to listen for
     * @param actionRowEmptier A {@link Runnable} which removes all action rows from the message
     * @see ColossusBuilder#setDefaultComponentListenerExpirationTime(long, TimeUnit)
     * @see #addListener(Long, List)
     * @see #addListener(Long, List, Runnable, long, TimeUnit)
     */
    public static void addListener(Long msgId, List<ButtonRow> buttonRows, Runnable actionRowEmptier) {
        addListener(msgId, buttonRows, actionRowEmptier,
            Colossus.getDefaultComponentListenerExpirationTimeAmount(), Colossus.getDefaultComponentListenerExpirationTimeUnit());
    }

    /**
     * Removes all listeners associated with the provided message
     * @param msgId The ID of the message where the buttons are located
     * @see #addListener(Long, List)
     * @see #addListener(Long, List, Runnable, long, TimeUnit)
     * @see #addListener(Long, List, Runnable)
     */
    public static void removeListeners(Long msgId) {
        if (MESSAGE_BUTTONS.containsKey(msgId)) MESSAGE_BUTTONS.remove(msgId).forEach(BUTTONS::remove);
    }

    public final ButtonInteractionEvent event;
    public final BaseButton button;

    public ButtonClickEvent(ButtonInteractionEvent event) {
        this.event = event;
        this.button = BUTTONS.get(new ButtonIdentifier(event.getMessageIdLong(), event.getButton().getId()));
    }

    /**
     * Handle this event; execute it
     */
    public void handle() throws CommandException {
        if (button == null) return;
        if (button.onClick() != null) button.onClick().accept(this);
    }

    @Override
    public Message getMessage() {
        return getEvent().getMessage();
    }

    public String getButtonId() {
        return event.getButton().getId();
    }

    private record ButtonIdentifier(Long msgId, String buttonId) {}
}
