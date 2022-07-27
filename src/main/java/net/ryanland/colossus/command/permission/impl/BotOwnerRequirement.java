package net.ryanland.colossus.command.permission.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.permission.UserPermissionRequirement;
import net.ryanland.colossus.sys.entities.ColossusUser;

public class BotOwnerRequirement implements UserPermissionRequirement {

    @Override
    public boolean check(ColossusUser entity) {
        return Colossus.getBotOwner().getId().equals(entity.getId());
    }

    @Override
    public String getName() {
        return "Bot Owner";
    }
}
