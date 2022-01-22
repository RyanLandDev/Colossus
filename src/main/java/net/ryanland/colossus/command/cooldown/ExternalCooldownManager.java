package net.ryanland.colossus.command.cooldown;

import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.Table;

import java.util.Collections;
import java.util.List;

public class ExternalCooldownManager implements CooldownManager {

    private static final ExternalCooldownManager INSTANCE = new ExternalCooldownManager();

    public static ExternalCooldownManager getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Cooldown> get(User user) {
        return Colossus.getCooldownsSerializer().deserialize(Colossus.getDatabaseDriver().get(user).get("_cd"));
    }

    @Override
    public void purge(User user) {
        Colossus.getDatabaseDriver().updateTable(user, Colossus.getDatabaseDriver().get(user).put("_cd", Collections.emptyList()));
    }

    @Override
    public Cooldown remove(User user, Cooldown cooldown) {
        Table<User> table = Colossus.getDatabaseDriver().get(user);

        List<Cooldown> cooldowns = get(user);
        cooldowns.remove(cooldown);

        table.put("_cd", Colossus.getCooldownsSerializer().serialize(cooldowns));
        Colossus.getDatabaseDriver().updateTable(user, table);

        return cooldown;
    }

    @Override
    public Cooldown put(User user, Cooldown cooldown) {
        Table<User> table = Colossus.getDatabaseDriver().get(user);

        List<Cooldown> cooldowns = get(user);
        cooldowns.add(cooldown);

        table.put("_cd", Colossus.getCooldownsSerializer().serialize(cooldowns));
        Colossus.getDatabaseDriver().updateTable(user, table);

        return cooldown;
    }

    @Override
    public List<Cooldown> put(User user, List<Cooldown> cooldowns) {
        Colossus.getDatabaseDriver().modifyTable(user, table ->
            table.put("_cd", Colossus.getCooldownsSerializer().serialize(cooldowns)));
        return cooldowns;
    }
}
