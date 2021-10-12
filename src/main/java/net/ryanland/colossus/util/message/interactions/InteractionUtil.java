package net.ryanland.colossus.util.message.interactions;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class InteractionUtil {

    public static List<ActionRow> of(List<Button> components) {
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
}
