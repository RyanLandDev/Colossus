package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActionButtonRow {

    public ActionButtonRow(ActionButton... buttons) {
        add(buttons);
    }

    private List<ActionButton> buttons = new ArrayList<>(5);

    public List<ActionButton> getButtons() {
        return buttons;
    }

    public void add(ActionButton... buttons) {
        this.buttons.addAll(List.of(buttons));
    }

    public void add(int index, ActionButton button) {
        buttons.add(index, button);
    }

    public int size() {
        return buttons.size();
    }

    public ActionButton get(int index) {
        return buttons.get(index);
    }

    public ActionRow toActionRow() {
        return ActionRow.of(getButtons().stream().map(ActionButton::button).collect(Collectors.toList()));
    }
}
