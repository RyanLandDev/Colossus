package net.ryanland.colossus.command.permission;

import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusUser;

public interface UserPermissionRequirement extends PermissionRequirement<ColossusUser> {

    @Override
    default ColossusUser fromCommandEvent(BasicCommandEvent event) {
        return event.getUser();
    }
}
