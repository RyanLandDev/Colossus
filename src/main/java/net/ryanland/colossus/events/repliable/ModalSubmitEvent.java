package net.ryanland.colossus.events.repliable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;

import java.util.HashMap;
import java.util.List;

public class ModalSubmitEvent implements EditableRepliableEvent {

    private static final HashMap<ModalIdentifier, CommandConsumer<ModalSubmitEvent>> MODAL_ACTIONS = new HashMap<>();

    /**
     * Adds a modal submit listener that will execute the provided action when the modal is submitted
     * @param userId The modal target user ID
     * @param modalId The modal ID
     * @param action The action to execute
     */
    public static void addListener(Long userId, String modalId, CommandConsumer<ModalSubmitEvent> action) {
        MODAL_ACTIONS.put(new ModalIdentifier(userId, modalId), action);
    }

    public final ModalInteractionEvent event;
    public final ModalIdentifier modalIdentifier;

    public ModalSubmitEvent(ModalInteractionEvent event) {
        this.event = event;
        this.modalIdentifier = new ModalIdentifier(event.getUser().getIdLong(), event.getModalId());
    }

    /**
     * Handle this event; execute it
     */
    public void handle() throws CommandException {
        CommandConsumer<ModalSubmitEvent> action = MODAL_ACTIONS.remove(modalIdentifier);
        if (action == null) return;
        action.accept(this);
    }

    @Override
    public ModalInteractionEvent getEvent() {
        return event;
    }

    @Override
    public Message getMessage() {
        return getEvent().getMessage();
    }

    public ModalIdentifier getModalIdentifier() {
        return modalIdentifier;
    }

    /**
     * Returns a List of {@link ModalMapping ModalMappings} representing the values input by the user for each field when the modal was submitted.
     *
     * @return Immutable List of {@link ModalMapping ModalMappings}
     *
     * @see    #getValue(String)
     */
    public List<ModalMapping> getValues() {
        return getEvent().getValues();
    }

    /**
     * Convenience method to get a {@link ModalMapping ModalMapping} by its id from the List of {@link net.dv8tion.jda.api.interactions.modals.ModalMapping ModalMappings}
     *
     * <p>Returns null if no component with that id has been found
     *
     * @param  id
     *         The custom id
     *
     * @throws IllegalArgumentException
     *         If the provided id is null
     *
     * @return ModalMapping with this id, or null if not found
     *
     * @see    #getValues()
     */
    public ModalMapping getValue(String id) {
        return getEvent().getValue(id);
    }

    private record ModalIdentifier(Long userId, String modalId) {}
}
