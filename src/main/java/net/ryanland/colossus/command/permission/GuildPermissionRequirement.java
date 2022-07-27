package net.ryanland.colossus.command.permission;

import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusGuild;

public interface GuildPermissionRequirement extends PermissionRequirement<ColossusGuild> {

    @Override
    default ColossusGuild fromCommandEvent(BasicCommandEvent event) {
        return event.getGuild();
    }
}
