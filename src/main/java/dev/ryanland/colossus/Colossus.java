package dev.ryanland.colossus;

import dev.ryanland.colossus.command.Category;
import dev.ryanland.colossus.command.Command;
import dev.ryanland.colossus.command.ContextCommand;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.command.finalizers.Finalizer;
import dev.ryanland.colossus.command.inhibitors.Inhibitor;
import dev.ryanland.colossus.events.repliable.ButtonClickEvent;
import dev.ryanland.colossus.events.repliable.SelectMenuEvent;
import dev.ryanland.colossus.sys.config.Config;
import dev.ryanland.colossus.sys.config.ConfigSupplier;
import dev.ryanland.colossus.sys.file.LocalFile;
import dev.ryanland.colossus.sys.interactions.select.BaseSelectMenu;
import dev.ryanland.colossus.sys.presetbuilder.PresetType;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.nio.file.InvalidPathException;
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
    @Getter
    private static Set<Category> categories;
    private static List<Command> commands;
    private static List<ContextCommand<?>> contextCommands;
    private static List<LocalFile> localFiles;
    private static long componentListenerExpirationTimeAmount;
    private static TimeUnit componentListenerExpirationTimeUnit;
    @Getter
    private static PresetType defaultPresetType;
    @Getter
    private static PresetType errorPresetType;
    @Getter
    private static PresetType successPresetType;
    private static LocalizationFunction localizationFunction;
    @Getter
    private static List<Inhibitor> inhibitors;
    @Getter
    private static List<Finalizer> finalizers;

    private final JDABuilder builder;

    @Getter
    private static User botOwner;

    public Colossus(JDABuilder builder, ConfigSupplier config, Set<Category> categories, List<Command> commands,
                    List<ContextCommand<?>> contextCommands, List<LocalFile> localFiles, long buttonListenerExpirationTimeAmount,
                    TimeUnit buttonListenerExpirationTimeUnit, PresetType defaultPresetType, PresetType errorPresetType,
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
            return;
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
            CommandHandler.upsertAll();
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

    /**
     * Returns the configured {@link ConfigSupplier}.
     * <p>If you're only looking to retrieve values from the config, use {@link Config} instead.
     */
    public static ConfigSupplier getConfig() {
        return config;
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

}