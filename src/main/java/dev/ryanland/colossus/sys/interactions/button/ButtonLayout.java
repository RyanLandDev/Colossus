package dev.ryanland.colossus.sys.interactions.button;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of {@link ButtonRow ButtonRows} with additional helper methods.
 */
public class ButtonLayout {

    public ButtonLayout(ButtonRow... rows) {
        add(rows);
    }

    private List<ButtonRow> rows = new ArrayList<>(5);

    public List<ButtonRow> getRows() {
        return rows;
    }

    public ButtonLayout add(ButtonRow... rows) {
        this.rows.addAll(List.of(rows));
        return this;
    }

    public ButtonLayout add(int index, ButtonRow row) {
        rows.add(index, row);
        return this;
    }

    public ButtonLayout addButton(int rowIndex, BaseButton... buttons) {
        rows.get(rowIndex).add(buttons);
        return this;
    }

    public ButtonLayout insertButton(int rowIndex, int buttonIndex, BaseButton button) {
        rows.get(rowIndex).add(buttonIndex, button);
        return this;
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
