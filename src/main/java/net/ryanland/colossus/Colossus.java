package net.ryanland.colossus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.internal.utils.JDALogger;
import net.ryanland.colossus.command.Category;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.finalizers.CommandFinalizer;
import net.ryanland.colossus.command.finalizers.ContextFinalizer;
import net.ryanland.colossus.command.inhibitors.CommandInhibitor;
import net.ryanland.colossus.command.inhibitors.ContextInhibitor;
import net.ryanland.colossus.events.ButtonClickEvent;
import net.ryanland.colossus.events.SelectMenuEvent;
import net.ryanland.colossus.sys.file.Config;
import net.ryanland.colossus.sys.file.LocalFile;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.Table;
import net.ryanland.colossus.sys.interactions.select.BaseSelectMenu;
import net.ryanland.colossus.sys.message.PresetType;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Main class for initializing Colossus
 * @author RyanLandDev
 */
public class Colossus {

    public static final Logger LOGGER = JDALogger.getLog("Colossus");
    public static final DiscordLocale DEFAULT_LOCALE = DiscordLocale.ENGLISH_US;

    private static JDA jda;
    private static Config config;
    private static Set<Category> categories;
    private static List<Command> commands;
    private static List<ContextCommand<?>> contextCommands;
    private static List<LocalFile> localFiles;
    private static long componentListenerExpirationTimeAmount;
    private static TimeUnit componentListenerExpirationTimeUnit;
    private static DatabaseDriver databaseDriver;
    private static PresetType defaultPresetType;
    private static PresetType errorPresetType;
    private static PresetType successPresetType;
    private static LocalizationFunction localizationFunction;
    private static List<CommandInhibitor> commandInhibitors;
    private static List<ContextInhibitor> contextInhibitors;
    private static List<CommandFinalizer> commandFinalizers;
    private static List<ContextFinalizer> contextFinalizers;

    private final JDABuilder builder;

    private static User botOwner;

    public Colossus(JDABuilder builder, Config config, Set<Category> categories, List<Command> commands,
                    List<ContextCommand<?>> contextCommands, List<LocalFile> localFiles, long buttonListenerExpirationTimeAmount,
                    TimeUnit buttonListenerExpirationTimeUnit, DatabaseDriver databaseDriver, PresetType defaultPresetType,
                    PresetType errorPresetType, PresetType successPresetType, LocalizationFunction localizationFunction,
                    List<CommandInhibitor> commandInhibitors, List<ContextInhibitor> contextInhibitors,
                    List<CommandFinalizer> commandFinalizers, List<ContextFinalizer> contextFinalizers) {
        this.builder = builder;

        Colossus.config = config;
        Colossus.categories = categories;
        Colossus.commands = commands;
        Colossus.contextCommands = contextCommands;
        Colossus.localFiles = localFiles;
        Colossus.componentListenerExpirationTimeAmount = buttonListenerExpirationTimeAmount;
        Colossus.componentListenerExpirationTimeUnit = buttonListenerExpirationTimeUnit;
        Colossus.databaseDriver = databaseDriver;
        Colossus.defaultPresetType = defaultPresetType;
        Colossus.errorPresetType = errorPresetType;
        Colossus.successPresetType = successPresetType;
        Colossus.localizationFunction = localizationFunction;
        Colossus.commandInhibitors = commandInhibitors;
        Colossus.contextInhibitors = contextInhibitors;
        Colossus.commandFinalizers = commandFinalizers;
        Colossus.contextFinalizers = contextFinalizers;
    }

    /**
     * Initialize the bot
     */
    public void initialize() {

        LOGGER.info("Initializing...");

        // Register commands
        CommandHandler.registerCommands(commands);
        CommandHandler.registerContextCommands(contextCommands);

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

        jda.retrieveApplicationInfo().queue(appInfo -> botOwner = appInfo.getOwner());

        LOGGER.info("Upserting " + (commands.size() + contextCommands.size()) + " commands...");

        // Upsert the registered slash and context commands
        CommandHandler.upsertAll();

        LOGGER.info("Initialized!");
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

    public static List<CommandInhibitor> getCommandInhibitors() {
        return commandInhibitors;
    }

    public static List<ContextInhibitor> getContextInhibitors() {
        return contextInhibitors;
    }

    public static List<CommandFinalizer> getCommandFinalizers() {
        return commandFinalizers;
    }

    public static List<ContextFinalizer> getContextFinalizers() {
        return contextFinalizers;
    }
}