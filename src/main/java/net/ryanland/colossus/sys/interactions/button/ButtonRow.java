package net.ryanland.colossus.sys.interactions.button;

import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonRow {

    public ButtonRow(BaseButton... buttons) {
        add(buttons);
    }

    private List<BaseButton> buttons = new ArrayList<>(5);

    public List<BaseButton> getButtons() {
        return buttons;
    }

    public void add(BaseButton... buttons) {
        this.buttons.addAll(List.of(buttons));
    }

    public void add(int index, BaseButton button) {
        buttons.add(index, button);
    }

    public int size() {
        return buttons.size();
    }

    public BaseButton get(int index) {
        return buttons.get(index);
    }

    public ActionRow toActionRow() {
        return ActionRow.of(getButtons().stream().map(BaseButton::button).collect(Collectors.toList()));
    }
}
