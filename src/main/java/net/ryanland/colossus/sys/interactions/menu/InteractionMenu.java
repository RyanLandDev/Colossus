package net.ryanland.colossus.sys.interactions.menu;

import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.events.repliable.RepliableEvent;

/**
 * Interface for interaction menus
 */
public interface InteractionMenu {

    void send(RepliableEvent event) throws CommandException;

}
