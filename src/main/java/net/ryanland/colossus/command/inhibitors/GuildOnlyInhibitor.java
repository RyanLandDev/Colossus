package net.ryanland.colossus.command.inhibitors;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.info.HelpMaker;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class GuildOnlyInhibitor implements Inhibitor {

    @Override
    public boolean check(CommandEvent event) {
        if (HelpMaker.getInfo(event.getCommand()).guildOnly())
            return true;
        else return !event.isFromGuild();
    }

    @Override
    public PresetBuilder buildMessage(CommandEvent event) {
        return new PresetBuilder(
                Colossus.getErrorPresetType(),
                "Server Only", "This command can only be executed in servers."
        );
    }
}
