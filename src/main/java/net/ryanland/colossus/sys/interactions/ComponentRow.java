package net.ryanland.colossus.sys.interactions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public abstract class ComponentRow {

    /**
     * Returns the {@link ActionRow} representative of this object
     */
    public abstract ActionRow toActionRow();

    /**
     * Adds a listener for this object
     */
    public abstract void startListening(Message message);
}
