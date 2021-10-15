package net.ryanland.colossus.sys.interactions.menu;

import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TabMenuBuilder implements InteractionMenuBuilder<TabMenu> {

    private final List<TabMenuPage> pages = new ArrayList<>(10);

    public TabMenuBuilder addPage(TabMenuPage page) {
        pages.add(page);
        return this;
    }

    public TabMenuBuilder insertPage(int index, TabMenuPage page) {
        pages.add(index, page);
        return this;
    }

    public TabMenuBuilder addPage(String name, PresetBuilder embed, String emoji, boolean hidden) {
        return addPage(new TabMenuPage(name, embed, emoji, hidden));
    }

    public TabMenuBuilder insertPage(int index, String name, PresetBuilder embed, String emoji, boolean hidden) {
        return insertPage(index, new TabMenuPage(name, embed, emoji, hidden));
    }

    public TabMenuBuilder addPage(String name, PresetBuilder embed, boolean hidden) {
        return addPage(name, embed, null, hidden);
    }

    public TabMenuBuilder insertPage(int index, String name, PresetBuilder embed, boolean hidden) {
        return insertPage(index, name, embed, null, hidden);
    }

    public TabMenuBuilder addPage(String name, PresetBuilder embed, String emoji) {
        return addPage(name, embed, emoji, false);
    }

    public TabMenuBuilder insertPage(int index, String name, PresetBuilder embed, String emoji) {
        return insertPage(index, name, embed, emoji, false);
    }

    public TabMenuBuilder addPage(String name, PresetBuilder embed) {
        return addPage(name, embed, null);
    }

    public TabMenuBuilder insertPage(int index, String name, PresetBuilder embed) {
        return insertPage(index, name, embed, null);
    }

    public TabMenuBuilder orderPages() {
        pages.sort(Comparator.comparing(TabMenuPage::getName));
        return this;
    }

    public TabMenu build() {
        return new TabMenu(pages);
    }
}
