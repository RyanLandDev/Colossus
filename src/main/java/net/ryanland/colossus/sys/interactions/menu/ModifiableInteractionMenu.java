package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;

public interface ModifiableInteractionMenu extends InteractionMenu {

    void edit(Message message);

    default void edit(ComponentInteraction interaction) {
        edit(interaction.getMessage());
    }
}
