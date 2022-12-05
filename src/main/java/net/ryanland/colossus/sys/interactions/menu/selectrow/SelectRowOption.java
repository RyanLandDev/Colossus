package net.ryanland.colossus.sys.interactions.menu.selectrow;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.ryanland.colossus.sys.interactions.ComponentRow;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.List;

public class SelectRowOption {

    private final String name;
    private final String description;
    private final Emoji emoji;
    private final PresetBuilder message;
    private final List<ComponentRow> rows;

    public SelectRowOption(String name, String description, Emoji emoji, PresetBuilder message) {
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.message = message;
        this.rows = message.getComponentRows();
    }

    public SelectRowOption(String name, String description, PresetBuilder message) {
        this(name, description, null, message);
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

    public PresetBuilder getMessage() {
        return message;
    }

    public List<ComponentRow> getRows() {
        return rows;
    }

    public SelectRowOption addRows(ComponentRow... rows) {
        this.rows.addAll(List.of(rows));
        return this;
    }
}
