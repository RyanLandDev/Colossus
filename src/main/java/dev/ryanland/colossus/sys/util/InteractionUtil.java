package dev.ryanland.colossus.sys.util;

import dev.ryanland.colossus.sys.interactions.button.BaseButton;
import dev.ryanland.colossus.sys.interactions.button.ButtonRow;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

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

    public static List<ButtonRow> ofBase(Collection<BaseButton> components) {
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
