package net.ryanland.colossus.sys.interactions.menu.tab;

import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.ryanland.colossus.sys.util.Node;

public class TabMenuPage extends Node<TabMenuPage> {

    private final String name;
    private final PresetBuilder embed;
    private final String emoji;
    private final boolean hidden;

    public TabMenuPage(String name, PresetBuilder embed, String emoji, boolean hidden) {
        this.name = name;
        this.embed = embed;
        this.emoji = emoji;
        this.hidden = hidden;
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