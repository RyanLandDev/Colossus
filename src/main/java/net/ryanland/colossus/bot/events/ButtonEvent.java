package net.ryanland.colossus.bot.events;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.bot.command.CommandException;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.interactions.ButtonHandler;
import org.jetbrains.annotations.NotNull;

public class ButtonEvent extends ListenerAdapter {

    public void onButtonClick(@NotNull ButtonClickEvent event) {
        try {
            ButtonHandler.handleEvent(event);
        } catch (CommandException e) {
            event.deferReply().addEmbeds(
                new PresetBuilder(Colossus.getErrorPresetType(), e.getMessage()).build()
            ).queue();
        }
    }

}
