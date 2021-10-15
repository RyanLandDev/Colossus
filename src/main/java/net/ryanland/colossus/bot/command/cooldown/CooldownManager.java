package net.ryanland.colossus.bot.command.cooldown;

import net.dv8tion.jda.api.entities.User;

import java.util.List;

public interface CooldownManager {

    List<Cooldown> get(User user);

    List<Cooldown> put(User user, List<Cooldown> cooldowns);

    Cooldown put(User user, Cooldown cooldown);

    Cooldown remove(User user, Cooldown cooldown);

    void purge(User user);
}
