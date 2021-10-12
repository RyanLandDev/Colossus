package net.ryanland.colossus.bot.events;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.bot.command.executor.CommandHandler;
import org.jetbrains.annotations.NotNull;

public class OnSlashCommandEvent extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        CommandHandler.run(new CommandEvent(event));
    }
}
