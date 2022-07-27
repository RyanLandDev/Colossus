package net.ryanland.colossus.command.permission.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.permission.PermissionRequirement;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusUser;

public class BotOwnerRequirement implements PermissionRequirement {

    @Override
    public boolean check(BasicCommandEvent event) {
        return Colossus.getBotOwner().getId().equals(event.getUser().getId());
    }

    @Override
    public String getName() {
        return "Bot Owner";
    }
}
