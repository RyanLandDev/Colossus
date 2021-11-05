package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.sys.interactions.ButtonClickContainer;
import net.ryanland.colossus.sys.interactions.ButtonHandler;
import net.ryanland.colossus.sys.interactions.InteractionUtil;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ActionMenu implements InteractionMenu {

    private final ActionButton[] actionButtons;
    private final PresetBuilder embed;

    public ActionMenu(List<ActionButton> actionButtons, PresetBuilder embed) {
        this(actionButtons.toArray(new ActionButton[0]), embed);
    }

    public ActionMenu(ActionButton[] actionButtons, PresetBuilder embed) {
        this.actionButtons = actionButtons;
        this.embed = embed;
    }

    public ActionButton[] getActionButtons() {
        return actionButtons;
    }

    @Override
    public void send(Interaction interaction) {
        // Stream map of actual buttons
        List<Button> buttons = Arrays.stream(actionButtons)
            .map(ActionButton::button)
            .collect(Collectors.toList());

        // Create container map
        HashMap<String, ButtonClickContainer> buttonConsumers = new HashMap<>();
        Arrays.asList(actionButtons)
            .forEach(button -> buttonConsumers.put(
                button.button().getId(), button.onClick()
            ));

        // Send the message and set the action rows
        InteractionHook hook = interaction.replyEmbeds(embed.build())
            .addActionRows(InteractionUtil.of(buttons))
            .complete();

        // Add button listener
        ButtonHandler.addListener(hook, buttonEvent -> new ButtonHandler.ButtonListener(
                interaction.getUser().getIdLong(),
                clickEvent -> buttonConsumers.get(clickEvent.getComponentId())
        ));
    }

    @Override
    public void send(Message message) throws CommandException {
        // Stream map of actual buttons
        List<Button> buttons = Arrays.stream(actionButtons)
            .map(ActionButton::button)
            .collect(Collectors.toList());

        // Create container map
        HashMap<String, ButtonClickContainer> buttonConsumers = new HashMap<>();
        Arrays.asList(actionButtons)
            .forEach(button -> buttonConsumers.put(
                button.button().getId(), button.onClick()
            ));

        // Send the message and set the action rows
        Message msg = message.replyEmbeds(embed.build())
            .setActionRows(InteractionUtil.of(buttons))
            .complete();

        // Add button listener
        ButtonHandler.addListener(msg, buttonEvent -> new ButtonHandler.ButtonListener(
                message.getAuthor().getIdLong(),
                clickEvent -> buttonConsumers.get(clickEvent.getComponentId())
        ));
    }
}
