package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.ryanland.colossus.command.executor.functional_interface.CommandBiConsumer;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.sys.interactions.ButtonClickContainer;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionMenuBuilder implements InteractionMenuBuilder<ActionMenu> {

    private final List<ActionButton> actionButtons = new ArrayList<>(25);
    private PresetBuilder embed;

    public ActionMenuBuilder setEmbed(PresetBuilder embed) {
        this.embed = embed;
        return this;
    }

    public List<ActionButton> getButtons() {
        return actionButtons;
    }

    public PresetBuilder getEmbed() {
        return embed;
    }

    public ActionMenuBuilder addButtons(ActionButton... buttons) {
        this.actionButtons.addAll(Arrays.asList(buttons));
        return this;
    }

    public ActionMenuBuilder addButtons(List<ActionButton> buttons) {
        return addButtons(buttons.toArray(new ActionButton[0]));
    }

    public ActionMenuBuilder addButton(ActionButton button) {
        actionButtons.add(button);
        return this;
    }

    public ActionMenuBuilder insertButton(int index, ActionButton button) {
        actionButtons.add(index, button);
        return this;
    }

    public ActionMenuBuilder addButton(Button button, ButtonClickContainer onClick) {
        return addButton(new ActionButton(button, onClick));
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

    public ActionMenuBuilder insertButton(int index, Button button, ButtonClickContainer onClick) {
        return insertButton(index, new ActionButton(button, onClick));
    }

    public <T> ActionMenuBuilder insertButton(int index, Button button,
                                              CommandBiConsumer<ButtonClickEvent, Object> onClick, T value) {
        return insertButton(index, button, new ButtonClickContainer(onClick, event -> value));
    }

    public ActionMenuBuilder insertButton(int index, Button button, CommandConsumer<ButtonClickEvent> onClick) {
        return insertButton(index, button, new ButtonClickContainer(onClick));
    }

    @Override
    public ActionMenu build() {
        return new ActionMenu(actionButtons, embed);
    }

}
