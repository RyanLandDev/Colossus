package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActionButtonLayout {

    public ActionButtonLayout(ActionButtonRow... rows) {
        add(rows);
    }

    private List<ActionButtonRow> rows = new ArrayList<>(5);

    public List<ActionButtonRow> getRows() {
        return rows;
    }

    public void add(ActionButtonRow... rows) {
        this.rows.addAll(List.of(rows));
    }

    public void add(int index, ActionButtonRow row) {
        rows.add(index, row);
    }

    public int size() {
        return rows.size();
    }

    public ActionButtonRow get(int index) {
        return rows.get(index);
    }

    public List<ActionRow> toActionRows() {
        return rows.stream().map(ActionButtonRow::toActionRow).collect(Collectors.toList());
    }

    public List<ActionButton> getActionButtons() {
        List<ActionButton> actionButtons = new ArrayList<>();
        rows.forEach(row -> actionButtons.addAll(row.getButtons()));
        return actionButtons;
    }

    public List<Button> getButtons() {
        return getActionButtons().stream().map(ActionButton::button).collect(Collectors.toList());
    }
}
