package net.ryanland.colossus.sys.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import net.ryanland.colossus.sys.database.annotations.DefaultTable;
import net.ryanland.colossus.sys.database.entities.ColossusEntity;
import net.ryanland.colossus.sys.database.entities.GuildEntity;
import net.ryanland.colossus.sys.database.entities.MemberEntity;
import net.ryanland.colossus.sys.database.entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;

public class HibernateManager {

    @Getter
    private static SessionFactory sessionFactory;
    @Getter
    private static List<Class<ColossusEntity>> entities = new ArrayList<>();

    private static Class<UserEntity> defaultUserTable;
    private static Class<MemberEntity> defaultMemberTable;
    private static Class<GuildEntity> defaultGuildTable;

    @Getter
    private static boolean initialized = false;

    /**
     * Initializes Hibernate with the given properties. See the Hibernate documentation for more info.
     * @param properties The properties to initialize Hibernate with
     *                   <p>Recommended:
     *                   <li>{@code hibernate.hikari.jdbcUrl} (<strong>Required</strong>)
     *                   <li>{@code hibernate.hikari.dataSource.username}
     *                   <li>{@code hibernate.hikari.dataSource.password}
     *                   <li>{@code hibernate.dataSource.driverClassName}
     *                   <li>{@code hibernate.dialect}
     *                   <li>{@code hibernate.hbm2ddl.auto}
     */
    public static void init(Map<String, String> properties) {
        if (initialized) throw new IllegalStateException("Hibernate has already been initialized.");
        initialized = true;

        Properties props = new Properties();
        props.setProperty("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
        props.setProperty("hibernate.hikari.connectionTimeout", "10000");
        props.setProperty("hibernate.hikari.minimumIdle", "20");
        props.setProperty("hibernate.hikari.maximumPoolSize", "300");
        props.setProperty("hibernate.hikari.idleTimeout", "200000");
        props.setProperty("hibernate.hbm2ddl.auto", "update");

        properties.forEach(props::setProperty);//may overwrite above properties

        var config = new Configuration().addProperties(props);
        entities.forEach(config::addAnnotatedClass);
        sessionFactory = config.buildSessionFactory();
    }

    public static void registerEntity(Class<? extends ColossusEntity> clazz) {
        entities.add((Class<ColossusEntity>) clazz);

        if (clazz.isAnnotationPresent(DefaultTable.class)) {
            if (UserEntity.class.isAssignableFrom(clazz)) {
                if (defaultUserTable != null) throw new IllegalStateException("Only one default user table may be configured.");
                defaultUserTable = (Class<UserEntity>) clazz.asSubclass(UserEntity.class);
            } else if (MemberEntity.class.isAssignableFrom(clazz)) {
                if (defaultMemberTable != null) throw new IllegalStateException("Only one default member table may be configured.");
                defaultMemberTable = (Class<MemberEntity>) clazz.asSubclass(MemberEntity.class);
            } else if (GuildEntity.class.isAssignableFrom(clazz)) {
                if (defaultGuildTable != null) throw new IllegalStateException("Only one default guild table may be configured.");
                defaultGuildTable = (Class<GuildEntity>) clazz.asSubclass(GuildEntity.class);
            }
        }
    }

    /**
     * Gets the user entity from the default user table
     */
    @SneakyThrows
    public static <R extends UserEntity> R getUser(String userId) {
        if (defaultUserTable == null) throw new IllegalStateException("A default user table has not been configured. Annotate the table class with @DefaultTable.");

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        UserEntity user = session.get(defaultUserTable, userId);

        // doesn't exist yet, so create a new record
        if (user == null) {
            try {
                user = defaultUserTable.getDeclaredConstructor().newInstance();
                user.setUserId(userId);
                session.persist(user);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("The default user table must have a no-args constructor.");
            }
        }

        session.getTransaction().commit();

        return (R) user;
    }

    /**
     * Gets the member entity from the default member table
     */
    @SneakyThrows
    public static <R extends MemberEntity> R getMember(String userId, String guildId) {
        if (defaultMemberTable == null) throw new IllegalStateException("A default member table has not been configured. Annotate the table class with @DefaultTable.");

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        MemberEntity member = session.get(defaultMemberTable, new MemberEntity.MemberEntityId(userId, guildId));

        // doesn't exist yet, so create a new record
        if (member == null) {
            try {
                member = defaultMemberTable.getDeclaredConstructor().newInstance();
                member.setUserId(userId);
                member.setGuildId(guildId);
                session.persist(member);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("The default member table must have a no-args constructor.");
            }
        }

        session.getTransaction().commit();

        return (R) member;
    }

    /**
     * Gets the guild entity from the default guild table
     */
    @SneakyThrows
    public static <R extends GuildEntity> R getGuild(String guildId) {
        if (defaultGuildTable == null) throw new IllegalStateException("A default guild table has not been configured. Annotate the table class with @DefaultTable.");

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        GuildEntity guild = session.get(defaultGuildTable, guildId);

        // doesn't exist yet, so create a new record
        if (guild == null) {
            try {
                guild = defaultGuildTable.getDeclaredConstructor().newInstance();
                guild.setGuildId(guildId);
                session.persist(guild);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("The default guild table must have a no-args constructor.");
            }
        }

        session.getTransaction().commit();

        return (R) guild;
    }

    /**
     * Updates the given entity in the database
     */
    public static void save(ColossusEntity entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.merge(entity);
        session.getTransaction().commit();
    }


}
