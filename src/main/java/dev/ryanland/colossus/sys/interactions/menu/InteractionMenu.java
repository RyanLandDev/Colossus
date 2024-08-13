package dev.ryanland.colossus.sys.interactions.menu;

import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.events.repliable.RepliableEvent;

/**
 * Interface for interaction menus
 */
public interface InteractionMenu {

    void send(RepliableEvent event) throws CommandException;

}
