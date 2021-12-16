package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.ryanland.colossus.command.executor.functional_interface.CommandBiConsumer;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.sys.interactions.ButtonClickContainer;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.List;

public class ActionMenuBuilder implements InteractionMenuBuilder<ActionMenu> {

    private final ActionButtonLayout layout = new ActionButtonLayout();
    private PresetBuilder embed;

    public ActionMenuBuilder setEmbed(PresetBuilder embed) {
        this.embed = embed;
        return this;
    }

    public ActionButtonLayout getLayout() {
        return layout;
    }

    public PresetBuilder getEmbed() {
        return embed;
    }

    public ActionMenuBuilder addButtons(ActionButton... buttons) {
        layout.add(new ActionButtonRow(buttons));
        return this;
    }

    public ActionMenuBuilder addButtons(List<ActionButton> buttons) {
        return addButtons(buttons.toArray(new ActionButton[0]));
    }

    public ActionMenuBuilder addRows(ActionButtonRow... actionRows) {
        layout.add(actionRows);
        return this;
    }

    public ActionMenuBuilder insertButton(int rowIndex, int buttonIndex, ActionButton button) {
        layout.get(rowIndex).add(buttonIndex, button);
        return this;
    }

    public ActionMenuBuilder addButton(Button button, ButtonClickContainer onClick) {
        return addButtons(new ActionButton(button, onClick));
    }

    public <T> ActionMenuBuilder addButton(Button button, CommandBiConsumer<ButtonClickEvent, Object> onClick, T value) {
        return addButton(button, new ButtonClickContainer(onClick, event -> value));
    }

    public ActionMenuBuilder addButton(Button button, CommandConsumer<ButtonClickEvent> onClick) {
        return addButton(button, new ButtonClickContainer(onClick));
    }

    public ActionMenuBuilder addButton(Button button) {
        return addButton(button, event -> {
        });
    }

    public ActionMenuBuilder insertButton(int rowIndex, int buttonIndex, Button button, ButtonClickContainer onClick) {
        return insertButton(rowIndex, buttonIndex, new ActionButton(button, onClick));
    }

    public <T> ActionMenuBuilder insertButton(int rowIndex, int buttonIndex, Button button,
                                              CommandBiConsumer<ButtonClickEvent, Object> onClick, T value) {
        return insertButton(rowIndex, buttonIndex, button, new ButtonClickContainer(onClick, event -> value));
    }

    public ActionMenuBuilder insertButton(int rowIndex, int buttonIndex, Button button, CommandConsumer<ButtonClickEvent> onClick) {
        return insertButton(rowIndex, buttonIndex, button, new ButtonClickContainer(onClick));
    }

    @Override
    public ActionMenu build() {
        return new ActionMenu(layout, embed);
    }

}
