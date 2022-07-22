package net.ryanland.colossus.command.cooldown;

import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.ColossusBuilder;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.Table;

import java.util.Collections;
import java.util.List;

/**
 * Database implementation of {@link CooldownManager}. Stores cooldowns using the configured {@link DatabaseDriver}.
 * @see ColossusBuilder#setDatabaseDriver(DatabaseDriver)
 */
public class DatabaseCooldownManager implements CooldownManager {

    private static final DatabaseCooldownManager INSTANCE = new DatabaseCooldownManager();
    private static final String COOLDOWNS_KEY = "_cd";

    public static DatabaseCooldownManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Cooldown> get(User user) {
        return Colossus.getCooldownsSerializer().deserialize(Colossus.getDatabaseDriver().get(user).get(COOLDOWNS_KEY));
    }

    @Override
    public void purge(User user) {
        Colossus.getDatabaseDriver().updateTable(user, Colossus.getDatabaseDriver().get(user).put(COOLDOWNS_KEY, Collections.emptyList()));
    }

    @Override
    public Cooldown remove(User user, Cooldown cooldown) {
        Table<User> table = Colossus.getDatabaseDriver().get(user);

        List<Cooldown> cooldowns = get(user);
        cooldowns.remove(cooldown);

        table.put(COOLDOWNS_KEY, Colossus.getCooldownsSerializer().serialize(cooldowns));
        Colossus.getDatabaseDriver().updateTable(user, table);

        return cooldown;
    }

    @Override
    public Cooldown put(User user, Cooldown cooldown) {
        Table<User> table = Colossus.getDatabaseDriver().get(user);

        List<Cooldown> cooldowns = get(user);
        cooldowns.add(cooldown);

        table.put(COOLDOWNS_KEY, Colossus.getCooldownsSerializer().serialize(cooldowns));
        Colossus.getDatabaseDriver().updateTable(user, table);

        return cooldown;
    }

    @Override
    public List<Cooldown> put(User user, List<Cooldown> cooldowns) {
        Colossus.getDatabaseDriver().modifyTable(user, table ->
            table.put(COOLDOWNS_KEY, Colossus.getCooldownsSerializer().serialize(cooldowns)));
        return cooldowns;
    }
}
