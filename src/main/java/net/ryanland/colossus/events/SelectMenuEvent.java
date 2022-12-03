package net.ryanland.colossus.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.ColossusBuilder;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.events.repliable.EditableRepliableEvent;
import net.ryanland.colossus.sys.interactions.select.BaseSelectMenu;
import net.ryanland.colossus.sys.util.ExecutorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

public class SelectMenuEvent implements EditableRepliableEvent {

    private static final HashMap<Long, List<SelectMenuIdentifier>> MESSAGE_SELECT_MENUS = new HashMap<>();
    private static final HashMap<SelectMenuIdentifier, BaseSelectMenu> SELECT_MENUS = new HashMap<>();

    /**
     * Adds a select menu listener that will listen indefinitely
     * @param msgId The ID of the message where the buttons are located
     * @param selectMenu The select menu to listen for
     * @see #addListener(Long, BaseSelectMenu, Runnable, long, TimeUnit)
     * @see #addListener(Long, BaseSelectMenu, Runnable)
     */
    public static void addListener(Long msgId, BaseSelectMenu selectMenu) {
        List<SelectMenuIdentifier> identifiers = MESSAGE_SELECT_MENUS.getOrDefault(msgId, new ArrayList<>());
        SelectMenuIdentifier identifier = new SelectMenuIdentifier(msgId, selectMenu.getSelectMenu().getId());
        SELECT_MENUS.put(identifier, selectMenu);
        identifiers.add(identifier);
        MESSAGE_SELECT_MENUS.put(msgId, identifiers);
    }

    /**
     * Adds a select menu listener that will stop listening after the provided time period
     * @param msgId The ID of the message where the buttons are located
     * @param selectMenu The select menu to listen for
     * @param actionRowEmptier A {@link Runnable} which removes all action rows from the message
     * @param timeAmount The amount of time to listen for
     * @param timeUnit The time unit used
     * @see #addListener(Long, BaseSelectMenu)
     * @see #addListener(Long, BaseSelectMenu, Runnable)
     */
    public static void addListener(Long msgId, BaseSelectMenu selectMenu,
                                   Runnable actionRowEmptier, long timeAmount, TimeUnit timeUnit) {
        addListener(msgId, selectMenu);
        ExecutorUtil.schedule(msgId.toString(), () -> {
            actionRowEmptier.run();
            removeListeners(msgId);
        }, timeAmount, timeUnit);
    }

    /**
     * Adds a select menu listener that will stop listening after the time period defined in {@link Colossus}
     * @param msgId The ID of the message where the buttons are located
     * @param selectMenu The select menu to listen for
     * @param actionRowEmptier A {@link Runnable} which removes all action rows from the message
     * @see ColossusBuilder#setDefaultComponentListenerExpirationTime(long, TimeUnit)
     * @see #addListener(Long, BaseSelectMenu)
     * @see #addListener(Long, BaseSelectMenu, Runnable, long, TimeUnit)
     */
    public static void addListener(Long msgId, BaseSelectMenu selectMenu, Runnable actionRowEmptier) {
        addListener(msgId, selectMenu, actionRowEmptier,
            Colossus.getDefaultComponentListenerExpirationTimeAmount(), Colossus.getDefaultComponentListenerExpirationTimeUnit());
    }

    /**
     * Removes all listeners associated with the provided message
     * @param msgId The ID of the message where the buttons are located
     * @see #addListener(Long, BaseSelectMenu)
     * @see #addListener(Long, BaseSelectMenu, Runnable, long, TimeUnit)
     * @see #addListener(Long, BaseSelectMenu, Runnable)
     */
    public static void removeListeners(Long msgId) {
        if (MESSAGE_SELECT_MENUS.containsKey(msgId)) MESSAGE_SELECT_MENUS.remove(msgId).forEach(SELECT_MENUS::remove);
    }

    public final GenericSelectMenuInteractionEvent<?, ?> event;
    public final SelectMenuIdentifier selectMenuIdentifier;

    public SelectMenuEvent(GenericSelectMenuInteractionEvent<?, ?> event) {
        this.event = event;
        this.selectMenuIdentifier = new SelectMenuIdentifier(event.getMessageIdLong(), event.getComponentId());
    }

    /**
     * Handle this event; execute it
     */
    public void handle() throws CommandException {
        CommandConsumer<SelectMenuEvent> action = SELECT_MENUS.get(selectMenuIdentifier).getOnSubmit();
        if (action == null) return;
        action.accept(this);
    }

    @Override
    public GenericSelectMenuInteractionEvent<?, ?> getEvent() {
        return event;
    }

    @Override
    public Message getMessage() {
        return getEvent().getMessage();
    }

    public SelectMenuIdentifier getSelectMenuIdentifier() {
        return selectMenuIdentifier;
    }

    public <T> List<T> getValues() {
        return (List<T>) event.getValues();
    }

    public <T> T getValue() {
        return (T) getValues().get(0);
    }

    private record SelectMenuIdentifier(long msgId, String selectMenuId) {}
}
