package net.ryanland.colossus.command.permissions;

import net.dv8tion.jda.api.entities.Member;
import net.ryanland.colossus.Colossus;

public class BotOwnerRequirement implements PermissionRequirement {

    @Override
    public boolean check(Member member) {
        return Colossus.getBotOwner().getId().equals(member.getId());
    }

    @Override
    public String getName() {
        return "Bot Owner";
    }
}
