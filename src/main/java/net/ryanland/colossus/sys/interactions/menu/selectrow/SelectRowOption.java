package net.ryanland.colossus.sys.interactions.menu.selectrow;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.ryanland.colossus.command.executor.functional_interface.CommandFunction;
import net.ryanland.colossus.events.repliable.RepliableEvent;
import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;

public class SelectRowOption {

    private final String name;
    private final String description;
    private final Emoji emoji;
    private final CommandFunction<RepliableEvent, PresetBuilder> message;

    public SelectRowOption(String name, String description, Emoji emoji, CommandFunction<RepliableEvent, PresetBuilder> message) {
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.message = message;
    }

    public SelectRowOption(String name, String description, PresetBuilder message) {
        this(name, description, null, event -> message);
    }

    public SelectRowOption(String name, PresetBuilder message) {
        this(name, null, message);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public CommandFunction<RepliableEvent, PresetBuilder> getMessage() {
        return message;
    }
}
