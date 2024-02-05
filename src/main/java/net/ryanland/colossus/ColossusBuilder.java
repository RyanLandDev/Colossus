package net.ryanland.colossus;

import com.google.gson.*;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.ryanland.colossus.command.Category;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import net.ryanland.colossus.command.finalizers.CooldownFinalizer;
import net.ryanland.colossus.command.finalizers.Finalizer;
import net.ryanland.colossus.command.impl.DefaultCommand;
import net.ryanland.colossus.command.impl.DefaultDisableCommand;
import net.ryanland.colossus.command.impl.DefaultEnableCommand;
import net.ryanland.colossus.command.impl.DefaultHelpCommand;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.command.inhibitors.impl.CooldownInhibitor;
import net.ryanland.colossus.command.inhibitors.impl.DisabledInhibitor;
import net.ryanland.colossus.command.inhibitors.impl.GuildOnlyInhibitor;
import net.ryanland.colossus.command.inhibitors.impl.PermissionInhibitor;
import net.ryanland.colossus.events.ButtonClickEvent;
import net.ryanland.colossus.events.InternalEventListener;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.sys.file.config.ConfigSupplier;
import net.ryanland.colossus.sys.file.local.LocalFile;
import net.ryanland.colossus.sys.file.config.JsonConfig;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.Provider;
import net.ryanland.colossus.sys.file.database.json.*;
import net.ryanland.colossus.sys.file.database.mongo.*;
import net.ryanland.colossus.sys.file.database.sql.*;
import net.ryanland.colossus.sys.message.DefaultPresetType;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.message.PresetType;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * A helper class for making a {@link Colossus} bot ready for startup
 */
public class ColossusBuilder {

    private static final Object[] CORE_EVENTS = new Object[]{
        new InternalEventListener()
    };

    private static final Inhibitor[] CORE_INHIBITORS = new Inhibitor[]{
        new DisabledInhibitor(),
        new PermissionInhibitor(),
        new CooldownInhibitor(),
        new GuildOnlyInhibitor()
    };

    private static final Finalizer[] CORE_FINALIZERS = new Finalizer[]{
        new CooldownFinalizer()
    };

    private static final LinkedHashMap<String, Object> CORE_CONFIG_ENTRIES = new LinkedHashMap<>();

    static {
        CORE_CONFIG_ENTRIES.put("token", "");

        CORE_CONFIG_ENTRIES.put("sharding.enabled", false);
        CORE_CONFIG_ENTRIES.put("sharding.shard_total", 1);

        CORE_CONFIG_ENTRIES.put("slash_commands.enabled", true);
        CORE_CONFIG_ENTRIES.put("slash_commands.global", false);
        CORE_CONFIG_ENTRIES.put("slash_commands.guild_id", "");

        CORE_CONFIG_ENTRIES.put("message_commands.enabled", false);
        CORE_CONFIG_ENTRIES.put("message_commands.prefix", "!");
    }

    private JDABuilder jdaBuilder;
    private ConfigSupplier config;
    private String configDirectory;
    private final Set<Category> categories = new LinkedHashSet<>();
    private final List<Command> commands = new ArrayList<>();
    private final List<ContextCommand<?>> contextCommands = new ArrayList<>();
    private final List<LocalFile> localFiles = new ArrayList<>();
    private final LinkedHashMap<String, Object> configEntries = CORE_CONFIG_ENTRIES;
    private final List<Inhibitor> inhibitors = new ArrayList<>();
    private final List<Finalizer> finalizers = new ArrayList<>();
    private final HashMap<String, Provider<?, ?>> providers = new HashMap<>();

    private boolean disableHelpCommand = false;
    private boolean disableCommandToggleCommands = false;
    private long buttonListenerExpirationTimeAmount = 2;
    private TimeUnit buttonListenerExpirationTimeUnit = TimeUnit.MINUTES;
    private DatabaseDriver databaseDriver = null;
    private PresetType defaultPresetType = DefaultPresetType.DEFAULT;
    private PresetType errorPresetType = DefaultPresetType.ERROR;
    private PresetType successPresetType = DefaultPresetType.SUCCESS;
    private LocalizationFunction localizationFunction = s -> Map.of();

    /**
     * Helper class to build a new instance of {@link Colossus} using a JSON config.<br>
     * You can use an alternate config system with the {@link #ColossusBuilder(ConfigSupplier)} constructor.
     * @param configDirectory The directory the config.json file should be in, containing the bot token among other things.<br>
     *                        For example, a valid input could be: "src/main/resources".<br>
     *                        This directory should be created manually before running your bot. When running, a config file
     *                        will be automatically generated with empty fields for you to fill in.<br><br>
     *
     *                        <strong>WARNING:</strong> It is recommended to {@code .gitignore} your config.json
     *                        if your code is public to prevent your bot token getting in hands of the wrong people.
     * @see Colossus
     * @see #ColossusBuilder(ConfigSupplier)
     */
    public ColossusBuilder(String configDirectory) {
        if (configDirectory == null) throw new NullPointerException("Provided config directory is null");

        // Get directory and perform checks
        LocalFile.validateDirectoryPath(configDirectory);
        this.configDirectory = configDirectory;

        // Prepare the config
        config = new JsonConfig(configDirectory + "/config.json");
        config.addValues(CORE_CONFIG_ENTRIES);
        config.read();

        // Prepare the builder
        jdaBuilder = JDABuilder.createDefault(config.getString("token"))
            .addEventListeners(CORE_EVENTS);
    }

    /**
     * Helper class to build a new instance of {@link Colossus} using a custom {@link ConfigSupplier} object.<br>
     * For a simpler approach using JSON, use the {@link #ColossusBuilder(String)} constructor.
     * @param config The {@link ConfigSupplier} implementation your bot should use.<br>
     *               Colossus provides one pre-built config implementation named {@link JsonConfig}.
     * @see Colossus
     */
    public ColossusBuilder(ConfigSupplier config) {
        // Prepare the config
        this.config = config;
        config.addValues(CORE_CONFIG_ENTRIES);
        config.read();

        // Prepare the builder
        jdaBuilder = JDABuilder.createDefault(config.getString("token"))
                .addEventListeners(CORE_EVENTS);
    }

    /**
     * Helper class to build a new instance of {@link Colossus} without a designated config.
     * <br>Note: using this constructor will let the bot ignore your {@code config.json} file.
     * <br>To use a config file instead, take advantage of the {@link #ColossusBuilder(String)} constructor.
     * <br>This constructor is mainly intended for quick setup. It is recommended to use a proper config file instead.
     * <br><br>
     * @param token The token of the bot. If you use this constructor, please make sure your bot files are not public,
     *              as this can lead anyone to being able to steal your bot token.<br><br>
     *
     *              Your bot's token can be retrieved by:<br>
     *              - Go to <a href="https://discord.com/developers/applications">Discord Developer Applications</a><br>
     *              - Click the bot you want to use<br>
     *              - Click <strong>Bot</strong> on the left side menu<br>
     *              - Under <i>Token</i>, click {@code Copy}<br>
     *              - Paste it here<br><br>
     * @param prefix The prefix of the bot, used for message commands.
     *               In addition to this prefix, the bot will also listen for mentions.<br><br>
     * @param guildId The ID of the Discord server you are running your bot in.<br><br>
     *
     *                  Your server's ID can be retrieved by:<br>
     *                  - Open Discord<br>
     *                  - Enable Developer Mode (User settings > Advanced > Developer Mode)<br>
     *                  - Right-click your server and click {@code Copy ID}<br>
     *                  - Paste it here<br>
     * @see Colossus
     */
    public ColossusBuilder(String token, String prefix, String guildId) {
        Map<String, JsonElement> values = new LinkedHashMap<>();

        values.put("token", toPrimitive(token));

        values.put("slash_commands.enabled", toPrimitive(guildId != null));
        values.put("slash_commands.global", toPrimitive(false));
        values.put("slash_commands.guild_id", toPrimitive(guildId));

        values.put("message_commands.enabled", toPrimitive(prefix != null));
        values.put("message_commands.prefix", toPrimitive(prefix));

        try { config = new JsonConfig(null); } catch (UnsupportedOperationException ignored) {}
        config.addValues(values);
        jdaBuilder = JDABuilder.createDefault(config.getString("token"))
            .addEventListeners(CORE_EVENTS);
    }

    private static JsonPrimitive toPrimitive(Object value) {
        if (value == null) return null;
        return (JsonPrimitive) JsonProvider.serializeElement(value);
    }

    /**
     * Build the {@link Colossus} bot.
     * <br>To initialize the bot, use {@link Colossus#initialize()} after building
     * @return The built {@link Colossus} instance
     * @see Colossus
     */
    public Colossus build() {
        // register default commands
        if (!disableHelpCommand || !disableCommandToggleCommands) {
            List<DefaultCommand> defaultCommands = new ArrayList<>();
            if (!disableHelpCommand) defaultCommands.add(new DefaultHelpCommand());
            if (!disableCommandToggleCommands) defaultCommands.addAll(List.of(new DefaultDisableCommand(), new DefaultEnableCommand()));
            registerCategories(new Category("Default", "These are the default commands provided by Colossus. " +
                "You can optionally disable them in your *ColossusBuilder*. If you want to give them a new category, " +
                "run ```java\nCommandHandler.getCommand(\"COMMAND_NAME\").setCategory(YOUR_CATEGORY);``` " +
                "**after** initializing your bot.",
                "⚠", defaultCommands.toArray(Command[]::new)));
        }

        // add core inhibitors and finalizers
        inhibitors.addAll(0, List.of(CORE_INHIBITORS));
        finalizers.addAll(0, List.of(CORE_FINALIZERS));

        // register core providers
        if (databaseDriver instanceof JsonDatabaseDriver) {
            registerCoreProviders(new JsonGlobalProvider(), new JsonGuildsProvider(), new JsonMembersProvider(), new JsonUsersProvider());
        } else if (databaseDriver instanceof MongoDatabaseDriver) {
            registerCoreProviders(new MongoGlobalProvider(), new MongoGuildsProvider(), new MongoMembersProvider(), new MongoUsersProvider());
        } else if (databaseDriver instanceof SQLDatabaseDriver) {
            registerCoreProviders(new SQLGlobalProvider(), new SQLGlobalProvider.DisabledCommandsProvider(),
                new SQLGuildsProvider(), new SQLMembersProvider(), new SQLUsersProvider(), new SQLUsersProvider.CooldownsProvider());
        }

        if (configDirectory != null) config.read();

        return new Colossus(jdaBuilder, config, categories, commands, contextCommands, localFiles,
            buttonListenerExpirationTimeAmount, buttonListenerExpirationTimeUnit, databaseDriver, providers,
            defaultPresetType, errorPresetType, successPresetType, localizationFunction, inhibitors, finalizers);
    }

    private void registerCoreProviders(Provider<?, ?>... providers) {
        for (Provider<?, ?> provider : providers) {
            if (!this.providers.containsKey(provider.getStockName())) {
                registerProviders(provider);
            }
        }
    }

    /**
     * Register {@link LocalFile}s
     * @param localFiles The file(s) to register
     * @return The builder
     * @see LocalFile
     * @see Colossus#getLocalFile(String)
     */
    public ColossusBuilder registerLocalFiles(LocalFile... localFiles) {
        this.localFiles.addAll(List.of(localFiles));
        return this;
    }

    /**
     * Register categories with commands
     * @param categories The categories to register
     * @return The builder
     * @see Category
     * @see Command
     */
    public ColossusBuilder registerCategories(Category... categories) {
        for (Category category : categories) {
            this.commands.addAll(category.getAllCommands());
            this.categories.add(category);
        }
        return this;
    }

    /**
     * Register context commands
     * @param contextCommands The context commands to register
     * @return The builder
     * @see ContextCommand
     */
    public ColossusBuilder registerContextCommands(ContextCommand<?>... contextCommands) {
        this.contextCommands.addAll(List.of(contextCommands));
        return this;
    }

    /**
     * Disables the default help command, optionally allowing you to create your own. This command is enabled by default.
     * @return The builder
     * @see DefaultHelpCommand
     */
    public ColossusBuilder disableHelpCommand() {
        disableHelpCommand = true;
        return this;
    }

    /**
     * Disables the default disable and enable commands, optionally allowing you to create your own.<br>
     * These commands are enabled by default.
     * <p>Note: A {@link SelfUser} (global) type must be present in the defined {@link DatabaseDriver}.
     * @return The builder
     * @see DefaultDisableCommand
     * @see DefaultEnableCommand
     * @see DisabledCommandHandler
     */
    public ColossusBuilder disableCommandToggleCommands() {
        disableCommandToggleCommands = true;
        return this;
    }

    /**
     * Modify the {@link JDABuilder} implementation used for this bot
     * @param modifier The function to use, with the currently defined {@link JDABuilder} given
     * @return This {@link ColossusBuilder}
     * @see JDABuilder
     */
    public ColossusBuilder setJDABuilder(Function<JDABuilder, JDABuilder> modifier) {
        jdaBuilder = modifier.apply(jdaBuilder);
        return this;
    }

    /**
     * Sets the {@link Activity Activity} for our session.
     * <br>This value can be changed at any time in the {@link net.dv8tion.jda.api.managers.Presence Presence} from a JDA instance.
     *
     * <p><b>Hint:</b> You can create an {@link Activity Activity} object using
     * {@link Activity#playing(String)} or {@link Activity#streaming(String, String)}.
     * @param  activity
     *         An instance of {@link Activity Activity} (null allowed)
     * @return This {@link ColossusBuilder}
     * @see    net.dv8tion.jda.api.managers.Presence#setActivity(Activity)  Presence.setActivity(Activity)
     */
    public ColossusBuilder setActivity(Activity activity) {
        setJDABuilder(builder -> builder.setActivity(activity));
        return this;
    }

    /**
     * Sets the {@link OnlineStatus} our connection will display.
     * <br>This value can be changed at any time in the {@link net.dv8tion.jda.api.managers.Presence Presence} from a JDA instance.
     * @param  status
     *         Not-null OnlineStatus (default online)
     * @return This {@link ColossusBuilder}
     */
    public ColossusBuilder setStatus(OnlineStatus status) {
        setJDABuilder(builder -> builder.setStatus(status));
        return this;
    }

    /**
     * Choose which {@link GatewayEncoding} JDA should use.
     * @param  encoding
     *         The {@link GatewayEncoding} (default: JSON)
     * @return This {@link ColossusBuilder}
     */
    public ColossusBuilder setGatewayEncoding(GatewayEncoding encoding) {
        setJDABuilder(builder -> builder.setGatewayEncoding(encoding));
        return this;
    }

    /**
     * Modify the default amount of time to wait before a button listener should expire.<br>
     * Expiring means removing the buttons from the message and stopping listeners.<br>
     * This value is only used in the {@link ButtonClickEvent#addListener(Long, List, Runnable)} method.<br>
     * By default, this value is set to <strong>2 minutes</strong>.
     * @return This {@link ColossusBuilder}
     * @see ButtonClickEvent
     * @see ButtonClickEvent#addListener(Long, List, Runnable)
     */
    public ColossusBuilder setDefaultComponentListenerExpirationTime(long timeAmount, TimeUnit timeUnit) {
        buttonListenerExpirationTimeAmount = timeAmount;
        buttonListenerExpirationTimeUnit = timeUnit;
        return this;
    }

    /**
     * Sets the {@link DatabaseDriver} used for this bot.<br>
     * This will affect the way database operations are made.
     * @param driver The driver to set to
     * @return The builder
     * @see DatabaseDriver
     */
    public ColossusBuilder setDatabaseDriver(DatabaseDriver driver) {
        databaseDriver = driver;
        return this;
    }

    /**
     * Register {@link Provider Providers}
     * @param providers The providers to register
     * @return The builder
     * @see Provider
     */
    public ColossusBuilder registerProviders(Provider<?, ?>... providers) {
        for (Provider<?, ?> provider : providers) {
            this.providers.put(provider.getStockName(), provider);
        }
        return this;
    }

    /**
     * Sets the default {@link PresetType} used in {@link PresetBuilder} when no type is specified.
     * <br>When this is not defined, {@link DefaultPresetType#DEFAULT} is used.
     * @param presetType The {@link PresetType} to set to
     * @return The builder
     * @see PresetType
     * @see PresetBuilder
     * @see DefaultPresetType
     */
    public ColossusBuilder setDefaultPresetType(PresetType presetType) {
        defaultPresetType = presetType;
        return this;
    }

    /**
     * Sets the default {@link PresetType} used when there is an internal error,
     * e.g. a command being on cooldown, {@link MalformedArgumentException}, {@link CommandException}, etc.
     * <br>When this is not defined, {@link DefaultPresetType#ERROR} is used.
     * @param presetType The {@link PresetType} to set to
     * @return The builder
     * @see PresetType
     * @see PresetBuilder
     * @see DefaultPresetType
     * @see CommandException
     */
    public ColossusBuilder setErrorPresetType(PresetType presetType) {
        errorPresetType = presetType;
        return this;
    }

    /**
     * Sets the default {@link PresetType} used when there was a successful action.
     * <br>When this is not defined, {@link DefaultPresetType#SUCCESS} is used.
     * @param presetType The {@link PresetType} to set to
     * @return The builder
     * @see PresetType
     * @see PresetBuilder
     * @see DefaultPresetType
     */
    public ColossusBuilder setSuccessPresetType(PresetType presetType) {
        successPresetType = presetType;
        return this;
    }

    /**
     * Sets the {@link LocalizationFunction} to use for slash and context commands, and optionally custom values.
     * <br>By default, no function is used.
     * @param localizationFunction The {@link LocalizationFunction} to set to
     * @return The builder
     * @see Colossus#getLocalization(DiscordLocale, String)
     * @see CommandEvent#getLocalization(String)
     * @see LocalizationFunction
     * @see ResourceBundleLocalizationFunction
     */
    public ColossusBuilder setLocalizationFunction(LocalizationFunction localizationFunction) {
        this.localizationFunction = localizationFunction;
        return this;
    }

    /**
     * Register {@link Inhibitor Inhibitors}
     * <br>Core inhibitors will be executed before custom ones. These are defined in {@code ColossusBuilder.CORE_INHIBITORS}
     * @param inhibitors The inhibitors to register
     * @return The builder
     * @see Inhibitor
     */
    public ColossusBuilder registerInhibitors(Inhibitor... inhibitors) {
        this.inhibitors.addAll(List.of(inhibitors));
        return this;
    }

    /**
     * Register {@link Finalizer Finalizers}
     * <br>Core finalizers will be executed before custom ones. These are defined in {@code ColossusBuilder.CORE_FINALIZERS}
     * @param finalizers The finalizers to register
     * @return The builder
     * @see Finalizer
     */
    public ColossusBuilder registerFinalizers(Finalizer... finalizers) {
        this.finalizers.addAll(List.of(finalizers));
        return this;
    }

    /**
     * Register custom entries that will appear in the {@code config.json} file.<br>
     * These can be retrieved later using the {@link ConfigSupplier} class.
     * <p>For sub-objects, use dots (e.g. "message_commands.prefix").
     * @param entries The entries to register (key-defaultValue pairs)
     * @return The builder
     * @see ConfigSupplier
     * @see ConfigSupplier#get(String)
     * @see ConfigSupplier#getString(String)
     * @see ConfigSupplier#getInt(String)
     * @see ConfigSupplier#getBoolean(String)
     */
    public ColossusBuilder registerConfigEntries(Map<String, Object> entries) {
        configEntries.putAll(entries);
        return this;
    }

    /**
     * Register custom entries that will appear in the {@code config.json} file.<br>
     * These can be retrieved later using the {@link ConfigSupplier} class.
     * @param key The config key. For sub-objects, use dots (e.g. "message_commands.prefix").
     * @param defaultValue The default config value
     * @return The builder
     * @see ConfigSupplier
     * @see ConfigSupplier#get(String)
     * @see ConfigSupplier#getString(String)
     * @see ConfigSupplier#getInt(String)
     * @see ConfigSupplier#getBoolean(String)
     */
    public ColossusBuilder registerConfigEntry(String key, Object defaultValue) {
        configEntries.put(key, defaultValue);
        return this;
    }

    /**
     * Disable the specified {@link GatewayIntent GatewayIntents}.
     * <br>This will not enable any currently unset intents.
     *
     * <p>If you disable certain intents you also have to disable related {@link CacheFlag CacheFlags}.
     * This can be achieved using {@link #disableCache(CacheFlag...)}. The required intents for each
     * flag are documented in the {@link CacheFlag} enum.
     *
     * @param intents The intents to disable
     * @return The builder
     * @see #enableIntents(GatewayIntent...)
     */
    public ColossusBuilder disableIntents(GatewayIntent... intents) {
        return setJDABuilder(builder -> builder.disableIntents(List.of(intents)));
    }

    /**
     * Enable the specified {@link GatewayIntent GatewayIntents}.
     * <br>This will not disable any currently set intents.
     * @param intents The intents to enable
     * @return The builder
     * @see #disableIntents(GatewayIntent...)
     */
    public ColossusBuilder enableIntents(GatewayIntent... intents) {
        return setJDABuilder(builder -> builder.enableIntents(List.of(intents)));
    }

    /**
     * Disable specific cache flags.
     * <br>This will not enable any currently unset cache flagss.
     * @param flags The {@link CacheFlag CacheFlags} to disable
     * @return The builder
     * @see #enableCache(CacheFlag...)
     */
    public ColossusBuilder disableCache(CacheFlag... flags) {
        return setJDABuilder(builder -> builder.disableCache(List.of(flags)));
    }

    /**
     * Enable specific cache flags.
     * <br>This will not disable any currently set cache flags.
     * @param flags The {@link CacheFlag CacheFlags} to enable
     * @return The builder
     * @see #disableCache(CacheFlag...)
     */
    public ColossusBuilder enableCache(CacheFlag... flags) {
        return setJDABuilder(builder -> builder.enableCache(List.of(flags)));
    }

    /**
     * Adds all provided listeners to the list of listeners that will be used to populate the {@link JDA} object.
     * <br>This uses the {@link net.dv8tion.jda.api.hooks.InterfacedEventManager InterfacedEventListener} by default.
     * <br>To switch to the {@link net.dv8tion.jda.api.hooks.AnnotatedEventManager AnnotatedEventManager},
     * use {@link JDABuilder#setEventManager(net.dv8tion.jda.api.hooks.IEventManager) setEventManager(new AnnotatedEventManager())}.
     *
     * <p><b>Note:</b> When using the {@link net.dv8tion.jda.api.hooks.InterfacedEventManager InterfacedEventListener} (default),
     * given listener(s) <b>must</b> be instance of {@link net.dv8tion.jda.api.hooks.EventListener EventListener}!
     *
     * @param listeners The listener(s) to add to the list.
     * @return The builder
     */
    public ColossusBuilder addEventListeners(Object... listeners) {
        return setJDABuilder(builder -> builder.addEventListeners(listeners));
    }

    /**
     * Returns the {@link ConfigSupplier} in its current state.<br>
     * This method should only be used before the bot is initialized.
     * @see Colossus#getConfig()
     */
    public ConfigSupplier getConfig() {
        return config;
    }

}
