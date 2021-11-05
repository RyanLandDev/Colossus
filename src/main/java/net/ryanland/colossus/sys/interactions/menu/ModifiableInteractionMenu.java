package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;

public interface ModifiableInteractionMenu extends InteractionMenu {

    void edit(Message message, User user);

    default void edit(ComponentInteraction interaction) {
        edit(interaction.getMessage(), interaction.getUser());
    }
}
