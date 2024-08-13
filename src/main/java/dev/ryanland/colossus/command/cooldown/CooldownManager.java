package dev.ryanland.colossus.command.cooldown;

import dev.ryanland.colossus.sys.snowflake.ColossusUser;

import java.util.List;

public interface CooldownManager {

    /**
     * Gets the active list of {@link Cooldown Cooldowns} for the provided {@link ColossusUser}
     */
    List<Cooldown> get(ColossusUser user);

    /**
     * Set the {@link ColossusUser}'s active {@link Cooldown Cooldowns}
     */
    List<Cooldown> put(ColossusUser user, List<Cooldown> cooldowns);

    /**
     * Add an active {@link Cooldown} to a {@link ColossusUser}
     */
    default Cooldown put(ColossusUser user, Cooldown cooldown) {
        List<Cooldown> cooldowns = get(user);
        cooldowns.add(cooldown);
        put(user, cooldowns);
        return cooldown;
    }

    /**
     * Remove an active {@link Cooldown} from a {@link ColossusUser}
     */
    Cooldown remove(ColossusUser user, Cooldown cooldown);

    /**
     * Remove all active cooldowns from a {@link ColossusUser}
     */
    void purge(ColossusUser user);
}
