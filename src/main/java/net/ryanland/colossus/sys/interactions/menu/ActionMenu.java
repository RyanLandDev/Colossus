package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.sys.interactions.ButtonClickContainer;
import net.ryanland.colossus.sys.interactions.ButtonHandler;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.HashMap;
import java.util.List;

public class ActionMenu implements ModifiableInteractionMenu {

    private final ActionButtonLayout layout;
    private final PresetBuilder embed;

    public ActionMenu(ActionButtonLayout actionRows, PresetBuilder embed) {
        this.layout = actionRows;
        this.embed = embed;
    }

    public ActionButtonLayout getActionRowLayout() {
        return layout;
    }

    @Override
    public void send(Interaction interaction) {
        // Get action buttons
        List<ActionButton> actionButtons = layout.getActionButtons();

        // Create container map
        HashMap<String, ButtonClickContainer> buttonConsumers = new HashMap<>();
        actionButtons.forEach(button -> buttonConsumers.put(button.button().getId(), button.onClick()));

        // Send the message and set the action rows
        InteractionHook hook = interaction.replyEmbeds(embed.build())
            .addActionRows(layout.toActionRows())
            .complete();

        // Add button listener
        ButtonHandler.addListener(hook, buttonEvent -> new ButtonHandler.ButtonListener(
                interaction.getUser().getIdLong(),
                clickEvent -> buttonConsumers.get(clickEvent.getComponentId())
        ));
    }

    @Override
    public void send(Message message) throws CommandException {
        // Get action buttons
        List<ActionButton> actionButtons = layout.getActionButtons();

        // Create container map
        HashMap<String, ButtonClickContainer> buttonConsumers = new HashMap<>();
        actionButtons.forEach(button -> buttonConsumers.put(button.button().getId(), button.onClick()));

        // Send the message and set the action rows
        Message msg = message.replyEmbeds(embed.build())
            .setActionRows(layout.toActionRows())
            .complete();

        // Add button listener
        ButtonHandler.addListener(msg, buttonEvent -> new ButtonHandler.ButtonListener(
                message.getAuthor().getIdLong(),
                clickEvent -> buttonConsumers.get(clickEvent.getComponentId())
        ));
    }

    @Override
    public void edit(Message message, User user) {
        // Get action buttons
        List<ActionButton> actionButtons = layout.getActionButtons();

        // Create container map
        HashMap<String, ButtonClickContainer> buttonConsumers = new HashMap<>();
        actionButtons.forEach(button -> buttonConsumers.put(button.button().getId(), button.onClick()));

        // Add button listener
        ButtonHandler.addListener(message.editMessageEmbeds(embed.build())
                .setActionRows(layout.toActionRows())
                .complete(),
            buttonEvent -> new ButtonHandler.ButtonListener(
                user.getIdLong(),
                clickEvent -> buttonConsumers.get(clickEvent.getComponentId())
            ));
    }
}
