package net.ryanland.colossus.events;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.context.ContextInteraction;
import net.ryanland.colossus.command.ContextCommand;

public class ContextCommandEvent<T extends ISnowflake> implements InteractionRepliableEvent {

    private final GenericContextInteractionEvent<T> event;
    private ContextCommand<T> command;

    public ContextCommandEvent(GenericContextInteractionEvent<T> event) {
        this.event = event;
    }

    public GenericContextInteractionEvent<T> getEvent() {
        return event;
    }

    public ContextCommand<T> getCommand() {
        return command;
    }

    public void setCommand(ContextCommand<T> contextCommand) {
        command = contextCommand;
    }

    @Override
    public IReplyCallback getCallback() {
        return getEvent();
    }

    /**
     * The target entity of this context interaction
     *
     * @return The target entity
     */
    public T getTarget() {
        return getEvent().getTarget();
    }

    /**
     * The target type of this context interaction
     *
     * @return The context target type
     */
    public ContextInteraction.ContextTarget getTargetType() {
        return getEvent().getTargetType();
    }

    /**
     * The {@link Interaction} instance.
     * <br>Note that this event is a delegate which implements the same interface.
     *
     * @return The {@link Interaction}
     */
    public ContextInteraction<T> getInteraction() {
        return getEvent().getInteraction();
    }

    public String getName() {
        return getInteraction().getName();
    }

}
