package net.ryanland.colossus.sys.interactions.button;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonLayout {

    public ButtonLayout(ButtonRow... rows) {
        add(rows);
    }

    private List<ButtonRow> rows = new ArrayList<>(5);

    public List<ButtonRow> getRows() {
        return rows;
    }

    public void add(ButtonRow... rows) {
        this.rows.addAll(List.of(rows));
    }

    public void add(int index, ButtonRow row) {
        rows.add(index, row);
    }

    public int size() {
        return rows.size();
    }

    public ButtonRow get(int index) {
        return rows.get(index);
    }

    public List<ActionRow> toActionRows() {
        return rows.stream().map(ButtonRow::toActionRow).toList();
    }

    public List<BaseButton> getBaseButtons() {
        List<BaseButton> buttons = new ArrayList<>();
        rows.forEach(row -> buttons.addAll(row.getButtons()));
        return buttons;
    }

    public List<Button> getButtons() {
        return getBaseButtons().stream().map(BaseButton::button).toList();
    }
}
