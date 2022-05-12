package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.Button;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.events.ClickButtonEvent;
import net.ryanland.colossus.events.RepliableEvent;
import net.ryanland.colossus.sys.interactions.button.BaseButton;
import net.ryanland.colossus.sys.message.PresetBuilder;

/**
 * Creates an {@link InteractionMenu} that presents the user with an option to either go through with an action or cancel it.
 * @param description The description of the embed to confirm the action
 * @param confirmedDescription The description of the embed after the user has clicked the Confirm button
 * @param confirmAction Code to perform when the user has clicked the Confirm button
 */
public record ConfirmMenu(String description, String confirmedDescription, CommandConsumer<ClickButtonEvent> confirmAction)
    implements InteractionMenu {

    @Override
    public void send(RepliableEvent event) {
        long userId = event.getUser().getIdLong();

        // Create embed
        PresetBuilder embed = new PresetBuilder()
            .setTitle("Confirm")
            .setDescription(description)
            .addLogo()
            .setEphemeral(true);

        // Create buttons
        embed.addButtons(
            BaseButton.user(userId, Button.success("confirm", "Confirm").withEmoji(Emoji.fromUnicode("✅")),
                evt -> {
                    evt.reply(embed.setDescription(confirmedDescription).clearButtons());
                    confirmAction.consume(evt);
                }),
            BaseButton.user(userId, Button.danger("dismiss", "Cancel").withEmoji(Emoji.fromUnicode("❎")),
                evt -> evt.reply(embed.setDescription("Action canceled.").clearButtons()))
        );

        // Send the message
        event.reply(embed);
    }
}
