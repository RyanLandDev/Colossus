package dev.ryanland.colossus.command.cooldown;

import dev.ryanland.colossus.sys.snowflake.ColossusUser;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Memory implementation of {@link CooldownManager}. Stores cooldowns in a private {@link HashMap}.
 * <br><strong>WARNING:</strong> This means these cooldowns will reset on restart!
 */
public class MemoryCooldownManager implements CooldownManager {

    private static final HashMap<User, List<Cooldown>> COOLDOWNS = new HashMap<>();
    private static final MemoryCooldownManager INSTANCE = new MemoryCooldownManager();

    public static MemoryCooldownManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Cooldown> get(ColossusUser user) {
        return COOLDOWNS.getOrDefault(user, new ArrayList<>());
    }

    @Override
    public List<Cooldown> put(ColossusUser user, List<Cooldown> cooldowns) {
        return COOLDOWNS.put(user, cooldowns);
    }

    @Override
    public Cooldown remove(ColossusUser user, Cooldown cooldown) {
        List<Cooldown> cooldowns = get(user);
        cooldowns.remove(cooldown);

        if (cooldowns.isEmpty()) {
            purge(user);
        } else {
            put(user, cooldown);
        }

        return cooldown;
    }

    @Override
    public void purge(ColossusUser user) {
        COOLDOWNS.remove(user);
    }
}
