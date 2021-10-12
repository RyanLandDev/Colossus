package net.ryanland.colossus.util.message.interactions.menu.tab;

import net.ryanland.colossus.util.message.builders.PresetBuilder;

public record TabMenuPage(String name, PresetBuilder embed, String emoji, boolean hidden) {

    public TabMenuPage(String name, PresetBuilder embed) {
        this(name, embed, null);
    }

    public TabMenuPage(String name, PresetBuilder embed, String emoji) {
        this(name, embed, emoji, false);
    }

    public TabMenuPage(String name, PresetBuilder embed, boolean hidden) {
        this(name, embed, null, hidden);
    }

    public String getName() {
        return name;
    }

    public PresetBuilder getEmbed() {
        return embed;
    }

    public String getEmoji() {
        return emoji;
    }

    public boolean isHidden() {
        return hidden;
    }
}
