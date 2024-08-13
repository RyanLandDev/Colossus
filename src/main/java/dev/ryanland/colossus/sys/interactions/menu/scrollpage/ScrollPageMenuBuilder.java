package dev.ryanland.colossus.sys.interactions.menu.scrollpage;

import dev.ryanland.colossus.sys.interactions.menu.InteractionMenuBuilder;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;

import java.util.ArrayList;
import java.util.List;

public class ScrollPageMenuBuilder implements InteractionMenuBuilder<ScrollPageMenu> {

    private final List<PresetBuilder> pages = new ArrayList<>();
    private int startPage = 0;

    public ScrollPageMenuBuilder() {
    }

    public ScrollPageMenuBuilder(PresetBuilder... pages) {
        addPages(pages);
    }

    public ScrollPageMenuBuilder setStartPage(int startPage) {
        this.startPage = startPage;
        return this;
    }

    public ScrollPageMenuBuilder addPages(PresetBuilder... pages) {
        this.pages.addAll(List.of(pages));
        return this;
    }

    public ScrollPageMenuBuilder addPages(int index, PresetBuilder... pages) {
        this.pages.addAll(index, List.of(pages));
        return this;
    }

    @Override
    public ScrollPageMenu build() {
        return new ScrollPageMenu(pages, startPage);
    }
}
