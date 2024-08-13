package dev.ryanland.colossus.sys.interactions.menu.tab;

import dev.ryanland.colossus.sys.interactions.menu.InteractionMenuBuilder;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TabMenuBuilder implements InteractionMenuBuilder<TabMenu> {

    private final List<TabMenuPage> pages = new ArrayList<>(10);
    private PresetBuilder homePage;

    public TabMenuBuilder addPages(TabMenuPage... pages) {
        this.pages.addAll(List.of(pages));
        return this;
    }

    public TabMenuBuilder insertPages(int index, TabMenuPage... pages) {
        this.pages.addAll(index, List.of(pages));
        return this;
    }

    /**
     * Sets the home page for this {@link TabMenu}.
     */
    public TabMenuBuilder setHomePage(PresetBuilder homePage) {
        this.homePage = homePage;
        return this;
    }

    public TabMenuBuilder orderPagesByName() {
        pages.sort(Comparator.comparing(TabMenuPage::getName));
        return this;
    }

    public TabMenu build() {
        return new TabMenu(pages, homePage);
    }
}
