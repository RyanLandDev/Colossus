package net.ryanland.colossus.events;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.sys.interactions.ButtonHandler;
import net.ryanland.colossus.sys.message.PresetBuilder;
import org.jetbrains.annotations.NotNull;

public class ColossusButtonEvent extends ListenerAdapter {

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
