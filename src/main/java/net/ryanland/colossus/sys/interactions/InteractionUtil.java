package net.ryanland.colossus.sys.interactions;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.ryanland.colossus.sys.interactions.button.BaseButton;
import net.ryanland.colossus.sys.interactions.button.ButtonRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class InteractionUtil {

    public static List<ActionRow> ofButton(List<Button> components) {
        Deque<Button> buttons = new ArrayDeque<>(components);
        List<Button> queue = new ArrayList<>();
        List<ActionRow> rows = new ArrayList<>();

        while (!buttons.isEmpty()) {
            queue.add(buttons.pop());
            if (queue.size() >= 5) {
                rows.add(ActionRow.of(queue));
                queue.clear();
            }
        }
        if (!queue.isEmpty()) {
            rows.add(ActionRow.of(queue));
        }

        return rows;
    }

    public static List<ButtonRow> ofBase(List<BaseButton> components) {
        Deque<BaseButton> buttons = new ArrayDeque<>(components);
        List<BaseButton> queue = new ArrayList<>();
        List<ButtonRow> rows = new ArrayList<>();

        while (!buttons.isEmpty()) {
            queue.add(buttons.pop());
            if (queue.size() >= 5) {
                rows.add(new ButtonRow(queue.toArray(BaseButton[]::new)));
                queue.clear();
            }
        }
        if (!queue.isEmpty()) rows.add(new ButtonRow(queue.toArray(BaseButton[]::new)));

        return rows;
    }
}
