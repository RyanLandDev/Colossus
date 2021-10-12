package net.ryanland.colossus.bot.events;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.bot.command.executor.exceptions.CommandException;
import net.ryanland.colossus.util.message.builders.PresetBuilder;
import net.ryanland.colossus.util.message.builders.PresetType;
import net.ryanland.colossus.util.message.interactions.ButtonHandler;
import org.jetbrains.annotations.NotNull;

public class ButtonEvent extends ListenerAdapter {

    public void onButtonClick(@NotNull ButtonClickEvent event) {
        try {
            ButtonHandler.handleEvent(event);

        } catch (CommandException e) {
            event.deferReply().addEmbeds(
                new PresetBuilder(PresetType.ERROR, e.getMessage()).build()
            ).setEphemeral(true).queue();
        }
    }

}
