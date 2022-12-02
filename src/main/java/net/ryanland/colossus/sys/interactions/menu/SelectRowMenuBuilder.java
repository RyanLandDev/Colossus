package net.ryanland.colossus.sys.interactions.menu;

import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SelectRowMenuBuilder implements InteractionMenuBuilder<SelectRowMenu> {

    private PresetBuilder startMessage;
    private List<SelectRowOption> options = new ArrayList<>();

    public SelectRowMenuBuilder(PresetBuilder startMessage, SelectRowOption... options) {
        this.startMessage = startMessage;
        this.options.addAll(List.of(options));
    }

    public PresetBuilder getStartMessage() {
        return startMessage;
    }

    public List<SelectRowOption> getOptions() {
        return options;
    }

    public SelectRowMenuBuilder setStartMessage(PresetBuilder startMessage) {
        this.startMessage = startMessage;
        return this;
    }

    public SelectRowMenuBuilder setOptions(List<SelectRowOption> options) {
        this.options = options;
        return this;
    }

    public SelectRowMenuBuilder addOptions(Collection<SelectRowOption> options) {
        this.options.addAll(options);
        return this;
    }

    public SelectRowMenuBuilder addOptions(SelectRowOption... options) {
        return addOptions(List.of(options));
    }

    @Override
    public SelectRowMenu build() {
        return new SelectRowMenu(options, startMessage);
    }
}
