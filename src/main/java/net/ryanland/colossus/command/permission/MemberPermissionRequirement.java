package net.ryanland.colossus.command.permission;

import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusMember;

public interface MemberPermissionRequirement extends PermissionRequirement<ColossusMember> {

    @Override
    default ColossusMember fromCommandEvent(BasicCommandEvent event) {
        return event.getMember();
    }
}
