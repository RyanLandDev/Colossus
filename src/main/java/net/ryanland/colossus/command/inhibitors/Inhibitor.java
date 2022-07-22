package net.ryanland.colossus.command.inhibitors;

import net.ryanland.colossus.events.RepliableEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public sealed interface Inhibitor<T extends RepliableEvent> permits CommandInhibitor, ContextInhibitor {

    boolean check(T event);

    PresetBuilder buildMessage(T event);
}
