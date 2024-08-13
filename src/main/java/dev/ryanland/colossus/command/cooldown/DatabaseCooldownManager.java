package dev.ryanland.colossus.command.cooldown;

import lombok.Getter;
import dev.ryanland.colossus.ColossusBuilder;
import dev.ryanland.colossus.command.CommandType;
import dev.ryanland.colossus.sys.database.HibernateManager;
import dev.ryanland.colossus.sys.snowflake.ColossusUser;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Database implementation of {@link CooldownManager}. Stores cooldowns using the configured database in Hibernate.
 * <p>Note: has to be enabled in the {@link ColossusBuilder} using {@link ColossusBuilder#enableDatabaseCooldowns}.
 * @see ColossusBuilder#initHibernate(Map)
 */
public class DatabaseCooldownManager implements CooldownManager {

    @Getter
    private static final DatabaseCooldownManager instance = new DatabaseCooldownManager();

    private void checkIfEnabled() {
        if (!HibernateManager.getEntities().contains(CooldownTable.class)) {
            throw new IllegalStateException("DatabaseCooldownManager is not enabled. Enable it using ColossusBuilder#enableDatabaseCooldowns.");
        }
    }

    @Override
    public List<Cooldown> get(ColossusUser user) {
        checkIfEnabled();

        Session session = HibernateManager.getSessionFactory().openSession();
        session.beginTransaction();
        List<CooldownTable> cooldowns = session.createQuery("FROM CooldownTable WHERE userId = :userId", CooldownTable.class)
                .setParameter("userId", user.getId())
                .list();
        session.getTransaction().commit();
        return new ArrayList<>(cooldowns.stream().map(cooldown -> new Cooldown(
            CommandType.of(cooldown.getCommandType()).getCommand(cooldown.getCommandName()), cooldown.getExpires())).toList());
    }

    @Override
    public void purge(ColossusUser user) {
        checkIfEnabled();

        Session session = HibernateManager.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM CooldownTable WHERE userId = :userId")
                .setParameter("userId", user.getId())
                .executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public Cooldown remove(ColossusUser user, Cooldown cooldown) {
        checkIfEnabled();

        Session session = HibernateManager.getSessionFactory().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM CooldownTable WHERE userId = :userId" +
                " AND commandName = :commandName AND commandType = :commandType")
                .setParameter("userId", user.getId())
                .setParameter("commandName", cooldown.command().getName())
                .setParameter("commandType", cooldown.command().getCommandType().getId())
                .executeUpdate();
        session.getTransaction().commit();
        return cooldown;
    }

    @Override
    public List<Cooldown> put(ColossusUser user, List<Cooldown> cooldowns) {
        checkIfEnabled();

        Session session = HibernateManager.getSessionFactory().openSession();
        session.beginTransaction();
        for (Cooldown cooldown : cooldowns) {
            CooldownTable cooldownTable = new CooldownTable(cooldown.command().getName(), cooldown.command().getCommandType().getId(), cooldown.expires());
            cooldownTable.setUserId(user.getId());
            session.persist(cooldownTable);
        }
        session.getTransaction().commit();
        return cooldowns;
    }
}
