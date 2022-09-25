package net.ryanland.colossus.command.cooldown;

import net.ryanland.colossus.ColossusBuilder;
import net.ryanland.colossus.sys.entities.ColossusUser;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Database implementation of {@link CooldownManager}. Stores cooldowns using the configured {@link DatabaseDriver}.
 * @see ColossusBuilder#setDatabaseDriver(DatabaseDriver)
 */
public class DatabaseCooldownManager implements CooldownManager {

    private static final DatabaseCooldownManager INSTANCE = new DatabaseCooldownManager();
    public static final String COOLDOWNS_KEY = "cooldowns";

    public static DatabaseCooldownManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Cooldown> get(ColossusUser user) {
        return user.getValue(COOLDOWNS_KEY, new ArrayList<>());
    }

    @Override
    public void purge(ColossusUser user) {
        user.updateValue(COOLDOWNS_KEY, Collections.emptyList());
    }

    @Override
    public Cooldown remove(ColossusUser user, Cooldown cooldown) {
        List<Cooldown> cooldowns = get(user);
        cooldowns.remove(cooldown);
        user.updateValue(COOLDOWNS_KEY, cooldowns);
        return cooldown;
    }

    @Override
    public List<Cooldown> put(ColossusUser user, List<Cooldown> cooldowns) {
        user.updateValue(COOLDOWNS_KEY, cooldowns);
        return cooldowns;
    }
}
