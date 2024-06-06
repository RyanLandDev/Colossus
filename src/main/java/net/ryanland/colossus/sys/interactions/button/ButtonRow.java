package net.ryanland.colossus.sys.interactions.button;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.ryanland.colossus.events.ButtonClickEvent;
import net.ryanland.colossus.sys.interactions.ComponentRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ButtonRow extends ComponentRow {

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

    @Override
    public ActionRow toActionRow() {
        return ActionRow.of(getButtons().stream().map(BaseButton::button).toList());
    }

    @Override
    public void startListening(Message message) {
        ButtonClickEvent.addListener(
            message.getIdLong(), List.of(this),
            () -> message.editMessageComponents(Collections.emptyList()).queue(success -> {}, error -> {})
        );
    }
}
