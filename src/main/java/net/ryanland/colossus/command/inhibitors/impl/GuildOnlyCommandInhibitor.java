package net.ryanland.colossus.command.inhibitors.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.info.HelpMaker;
import net.ryanland.colossus.command.inhibitors.CommandInhibitor;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class GuildOnlyCommandInhibitor implements CommandInhibitor {

    @Override
    public boolean check(CommandEvent event) {
        if (HelpMaker.getInfo(event.getCommand()).guildOnly())
            return !event.isFromGuild();
        else return false;
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(
                Colossus.getErrorPresetType(),
                "Server Only", "This command can only be executed in servers."
        );
    }
}
