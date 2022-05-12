package net.ryanland.colossus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.JDALogger;
import net.ryanland.colossus.command.Category;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.cooldown.Cooldown;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.finalizers.Finalizer;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.events.ClickButtonEvent;
import net.ryanland.colossus.sys.file.Config;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.LocalFile;
import net.ryanland.colossus.sys.file.database.Table;
import net.ryanland.colossus.sys.file.serializer.Serializer;
import net.ryanland.colossus.sys.message.PresetType;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Main class for initializing Colossus
 * @author RyanLandDev
 */
public class Colossus {

    private static final Logger LOGGER =
        JDALogger.getLog(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass());

    private static JDA jda;
    private static Config config;
    private static Set<Category> categories;
    private static List<Command> commands;
    private static List<LocalFile> localFiles;
    private static long buttonListenerExpirationTimeAmount;
    private static TimeUnit buttonListenerExpirationTimeUnit;
    private static DatabaseDriver databaseDriver;
    private static PresetType defaultPresetType;
    private static PresetType errorPresetType;
    private static PresetType successPresetType;
    private static Serializer<?, List<Cooldown>> cooldownsSerializer;
    private static Serializer<?, List<Command>> disabledCommandsSerializer;
    private static List<Inhibitor> inhibitors;
    private static List<Finalizer> finalizers;

    private final JDABuilder builder;

    private static User botOwner;

    public Colossus(JDABuilder builder, Config config, Set<Category> categories, List<Command> commands,
                    List<LocalFile> localFiles, long buttonListenerExpirationTimeAmount, TimeUnit buttonListenerExpirationTimeUnit,
                    DatabaseDriver databaseDriver, PresetType defaultPresetType, PresetType errorPresetType,
                    PresetType successPresetType, Serializer<?, List<Cooldown>> cooldownsSerializer,
                    Serializer<?, List<Command>> disabledCommandsSerializer, List<Inhibitor> inhibitors,
                    List<Finalizer> finalizers) {
        this.builder = builder;

        Colossus.config = config;
        Colossus.categories = categories;
        Colossus.commands = commands;
        Colossus.localFiles = localFiles;
        Colossus.buttonListenerExpirationTimeAmount = buttonListenerExpirationTimeAmount;
        Colossus.buttonListenerExpirationTimeUnit = buttonListenerExpirationTimeUnit;
        Colossus.databaseDriver = databaseDriver;
        Colossus.defaultPresetType = defaultPresetType;
        Colossus.errorPresetType = errorPresetType;
        Colossus.successPresetType = successPresetType;
        Colossus.cooldownsSerializer = cooldownsSerializer;
        Colossus.disabledCommandsSerializer = disabledCommandsSerializer;
        Colossus.inhibitors = inhibitors;
        Colossus.finalizers = finalizers;
    }

    /**
     * Initialize the bot
     */
    public void initialize() {

        // Register commands
        CommandHandler.register(commands);

        // Build the bot
        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
            LOGGER.error("Please put a valid token in the config.json file!");
            System.exit(0);
        }
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        botOwner = jda.retrieveApplicationInfo().complete().getOwner();

        // Upsert the registered slash commands
        CommandHandler.upsertAll();
    }

    // Utility methods ------------------------------

    public static Logger getLogger() {
        return LOGGER;
    }

    public static JDA getJda() {
        return jda;
    }

    public static SelfUser getSelfUser() {
        return jda.getSelfUser();
    }

    public static User getBotOwner() {
        return botOwner;
    }

    public static Config getConfig() {
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
            return getLocalFiles().stream()
                .filter(file -> file.getName().equals(name))
                .collect(Collectors.toList())
                .get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidPathException(name, "This local file does not exist or is not registered.");
        }
    }

    /**
     * Get the default button listener expiration time amount
     * @see #getDefaultButtonListenerExpirationTimeUnit()
     * @see ColossusBuilder#setDefaultButtonListenerExpirationTime(long, TimeUnit)
     * @see ClickButtonEvent#addListener(Long, List, Runnable)
     */
    public static long getDefaultButtonListenerExpirationTimeAmount() {
        return buttonListenerExpirationTimeAmount;
    }

    /**
     * Get the default button listener expiration time unit
     * @see #getDefaultButtonListenerExpirationTimeAmount()
     * @see ColossusBuilder#setDefaultButtonListenerExpirationTime(long, TimeUnit)
     * @see ClickButtonEvent#addListener(Long, List, Runnable)
     */
    public static TimeUnit getDefaultButtonListenerExpirationTimeUnit() {
        return buttonListenerExpirationTimeUnit;
    }

    /**
     * Get the configured {@link DatabaseDriver}
     * @see ColossusBuilder#setDatabaseDriver(DatabaseDriver)
     * @see DatabaseDriver
     */
    public static DatabaseDriver getDatabaseDriver() {
        if (databaseDriver == null)
            throw new IllegalStateException("A database driver has not been defined.");
        return databaseDriver;
    }

    /**
     * Get the {@link SelfUser} (global) table from the database
     * @see Table
     * @see DatabaseDriver
     * @see SelfUser
     */
    public static Table<SelfUser> getGlobalTable() {
        return getDatabaseDriver().get(getSelfUser());
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

    public static Serializer<?, List<Cooldown>> getCooldownsSerializer() {
        return cooldownsSerializer;
    }

    public static Serializer<?, List<Command>> getDisabledCommandsSerializer() {
        return disabledCommandsSerializer;
    }

    public static List<Inhibitor> getInhibitors() {
        return inhibitors;
    }

    public static List<Finalizer> getFinalizers() {
        return finalizers;
    }
}
