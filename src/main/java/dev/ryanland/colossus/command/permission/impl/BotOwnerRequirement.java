package dev.ryanland.colossus.command.permission.impl;

import dev.ryanland.colossus.command.permission.PermissionRequirement;
import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.events.command.BasicCommandEvent;

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
