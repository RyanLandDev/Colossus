package net.ryanland.colossus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.internal.utils.JDALogger;
import net.ryanland.colossus.command.Category;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.finalizers.Finalizer;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.events.ButtonClickEvent;
import net.ryanland.colossus.events.SelectMenuEvent;
import net.ryanland.colossus.sys.file.config.Config;
import net.ryanland.colossus.sys.file.config.ConfigSupplier;
import net.ryanland.colossus.sys.file.local.LocalFile;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.Provider;
import net.ryanland.colossus.sys.file.database.Supply;
import net.ryanland.colossus.sys.file.database.sql.SQLDatabaseDriver;
import net.ryanland.colossus.sys.interactions.select.BaseSelectMenu;
import net.ryanland.colossus.sys.message.PresetType;
import org.slf4j.Logger;

import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Main class for initializing Colossus
 * @author RyanLandDev
 * @see ColossusBuilder
 */
public class Colossus {

    public static final Logger LOGGER = JDALogger.getLog("Colossus");
    public static final DiscordLocale DEFAULT_LOCALE = DiscordLocale.ENGLISH_US;

    private static JDA jda;
    private static ConfigSupplier config;
    private static Set<Category> categories;
    private static List<Command> commands;
    private static List<ContextCommand<?>> contextCommands;
    private static List<LocalFile> localFiles;
    private static long componentListenerExpirationTimeAmount;
    private static TimeUnit componentListenerExpirationTimeUnit;
    private static DatabaseDriver databaseDriver;
    private static HashMap<String, Provider<?, ?>> providers;
    private static PresetType defaultPresetType;
    private static PresetType errorPresetType;
    private static PresetType successPresetType;
    private static LocalizationFunction localizationFunction;
    private static List<Inhibitor> inhibitors;
    private static List<Finalizer> finalizers;

    private final JDABuilder builder;

    private static User botOwner;

    public Colossus(JDABuilder builder, ConfigSupplier config, Set<Category> categories, List<Command> commands,
                    List<ContextCommand<?>> contextCommands, List<LocalFile> localFiles, long buttonListenerExpirationTimeAmount,
                    TimeUnit buttonListenerExpirationTimeUnit, DatabaseDriver databaseDriver,
                    HashMap<String, Provider<?, ?>> providers, PresetType defaultPresetType, PresetType errorPresetType,
                    PresetType successPresetType, LocalizationFunction localizationFunction, List<Inhibitor> inhibitors,
                    List<Finalizer> finalizers) {
        this.builder = builder;

        Colossus.config = config;
        Colossus.categories = categories;
        Colossus.commands = commands;
        Colossus.contextCommands = contextCommands;
        Colossus.localFiles = localFiles;
        Colossus.componentListenerExpirationTimeAmount = buttonListenerExpirationTimeAmount;
        Colossus.componentListenerExpirationTimeUnit = buttonListenerExpirationTimeUnit;
        Colossus.databaseDriver = databaseDriver;
        Colossus.providers = providers;
        Colossus.defaultPresetType = defaultPresetType;
        Colossus.errorPresetType = errorPresetType;
        Colossus.successPresetType = successPresetType;
        Colossus.localizationFunction = localizationFunction;
        Colossus.inhibitors = inhibitors;
        Colossus.finalizers = finalizers;
    }

    /**
     * Initialize the bot without sharding
     */
    public void initialize() {
        initialize(-1);
    }

    /**
     * Initialize the bot with sharding
     */
    public void initialize(int shard) {
        // check if sharding is disabled
        if (!Objects.requireNonNullElse(config.getBoolean("sharding.enabled"), false)) shard = -1;

        if (shard == -1) LOGGER.info("Initializing...");
        else LOGGER.info("Initializing shard #" + shard + "...");

        // only register commands if sharding is not used (-1) or if this is the first shard
        if (shard == -1 || shard == 0) {
            // Register commands
            CommandHandler.registerCommands(commands);
            CommandHandler.registerContextCommands(contextCommands);
        }

        // Build the bot
        try {
            if (shard != -1) builder.useSharding(shard, config.getInt("sharding.shard_total"));
            jda = builder.build();
        } catch (InvalidTokenException e) {
            Colossus.LOGGER.error("The token in your configuration is invalid.");
            System.exit(0);
        }
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (shard == -1 || shard == 0) {
            jda.retrieveApplicationInfo().queue(appInfo -> botOwner = appInfo.getOwner());
            LOGGER.info("Upserting " + (commands.size() + contextCommands.size()) + " commands...");
            // Upsert the registered slash and context commands
            try {
                CommandHandler.upsertAll();
            } catch (IllegalArgumentException e) {
                if (getDatabaseDriver() instanceof SQLDatabaseDriver) {
                    // default sql databases don't exist, create them
                    getSQLDatabaseDriver().query("create table global ( _bot_id varchar(25) constraint global_pk primary key )");
                    getSQLDatabaseDriver().query("create table guilds ( _guild_id varchar(25) constraint guilds_pk primary key )");
                    getSQLDatabaseDriver().query("create table members ( _user_id varchar(25) not null, _guild_id varchar(25) not null, constraint members_pk primary key (_guild_id, _user_id) )");
                    getSQLDatabaseDriver().query("create table users ( _user_id varchar(25) constraint users_pk primary key )");
                    getSQLDatabaseDriver().query("create table cooldowns ( user_id varchar(25) not null, command_name varchar(32) not null, command_type tinyint not null, expires datetime not null, constraint cooldowns_pk primary key (user_id, command_name, command_type) )");
                    getSQLDatabaseDriver().query("create table disabled_commands ( command_name varchar(32) not null, command_type tinyint not null, constraint disabled_commands_pk primary key (command_name, command_type) )");
                    LOGGER.info("Default SQL tables not found, created them");
                }
            }
            LOGGER.info("All commands upserted!");
        }

        if (shard == -1) LOGGER.info("Initialized!");
        else LOGGER.info("Initialized shard #" + shard + "!");
    }

    // Utility methods ------------------------------

    public static JDA getJDA() {
        return jda;
    }

    public static SelfUser getSelfUser() {
        return jda.getSelfUser();
    }

    public static User getBotOwner() {
        return botOwner;
    }

    /**
     * Returns the configured {@link ConfigSupplier}.
     * <p>If you're only looking to retrieve values from the config, use {@link Config} instead.
     */
    public static ConfigSupplier getConfig() {
        return config;
    }

    public static Set<Category> getCategories() {
        return categories;
    }

    /**
     * Get a list of all registered {@link LocalFile}s
     * @see ColossusBuilder#registerLocalFiles(LocalFile...)
     */
    public static List<LocalFile> getLocalFiles() {
        return localFiles;
    }

    /**
     * Get one of the registered {@link LocalFile}s
     * <br>
     * @param name The name of the {@link LocalFile} to get
     * @return The associated local file
     * @throws InvalidPathException If this file does not exist or is not registered
     * @see ColossusBuilder#registerLocalFiles(LocalFile...)
     */
    public static LocalFile getLocalFile(String name) {
        try {
            return getLocalFiles().stream().filter(file -> file.getName().equals(name)).toList().get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidPathException(name, "This local file does not exist or is not registered.");
        }
    }

    /**
     * Get the default component listener expiration time amount
     * @see #getDefaultComponentListenerExpirationTimeUnit()
     * @see ColossusBuilder#setDefaultComponentListenerExpirationTime(long, TimeUnit)
     * @see ButtonClickEvent#addListener(Long, List, Runnable)
     * @see SelectMenuEvent#addListener(Long, BaseSelectMenu, Runnable)
     */
    public static long getDefaultComponentListenerExpirationTimeAmount() {
        return componentListenerExpirationTimeAmount;
    }

    /**
     * Get the default component listener expiration time unit
     * @see #getDefaultComponentListenerExpirationTimeAmount()
     * @see ColossusBuilder#setDefaultComponentListenerExpirationTime(long, TimeUnit)
     * @see ButtonClickEvent#addListener(Long, List, Runnable)
     * @see SelectMenuEvent#addListener(Long, BaseSelectMenu, Runnable)
     */
    public static TimeUnit getDefaultComponentListenerExpirationTimeUnit() {
        return componentListenerExpirationTimeUnit;
    }

    /**
     * Get the configured {@link DatabaseDriver}
     * @see ColossusBuilder#setDatabaseDriver(DatabaseDriver)
     * @see DatabaseDriver
     * @see #getSQLDatabaseDriver()
     */
    public static DatabaseDriver getDatabaseDriver() {
        if (databaseDriver == null) throw new IllegalStateException("A database driver has not been defined.");
        return databaseDriver;
    }

    /**
     * Casts the result of {@link #getDatabaseDriver()} to a {@link SQLDatabaseDriver}
     * @see ColossusBuilder#setDatabaseDriver(DatabaseDriver)
     * @see DatabaseDriver
     * @see #getDatabaseDriver()
     */
    public static SQLDatabaseDriver getSQLDatabaseDriver() {
        return (SQLDatabaseDriver) getDatabaseDriver();
    }

    /**
     * Get the configured {@link Provider Providers}
     * @see ColossusBuilder#registerProviders(Provider...)
     * @see Provider
     */
    public static HashMap<String, Provider<?, ?>> getProviders() {
        return providers;
    }

    /**
     * Get one of the configured {@link Provider Providers} using its stock name
     * @see ColossusBuilder#registerProviders(Provider...)
     * @see Provider
     */
    @SuppressWarnings("unchecked")
    public static <R extends Provider<S1, S2>, S1, S2> R getProvider(String stockName) {
        return (R) providers.get(stockName);
    }

    /**
     * Get the global {@link Supply} from the database
     * @see Supply
     * @see DatabaseDriver
     */
    public static Supply getGlobalSupply() {
        return getDatabaseDriver().get("global").get(getSelfUser().getId());
    }

    public static PresetType getDefaultPresetType() {
        return defaultPresetType;
    }

    public static PresetType getErrorPresetType() {
        return errorPresetType;
    }

    public static PresetType getSuccessPresetType() {
        return successPresetType;
    }

    /**
     * Get the configured {@link LocalizationFunction}
     * @see ColossusBuilder#setLocalizationFunction(LocalizationFunction)
     * @see #getLocalization(DiscordLocale, String)
     * @see LocalizationFunction
     */
    public static LocalizationFunction getLocalizationFunction() {
        return localizationFunction;
    }

    public static String getLocalization(DiscordLocale locale, String key) {
        return getLocalizationFunction().apply(key).get(locale);
    }

    public static List<Inhibitor> getInhibitors() {
        return inhibitors;
    }

    public static List<Finalizer> getFinalizers() {
        return finalizers;
    }
}