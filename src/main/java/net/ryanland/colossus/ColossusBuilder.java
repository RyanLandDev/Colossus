package net.ryanland.colossus;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.bot.command.impl.Command;
import net.ryanland.colossus.bot.command.impl.info.HelpCommand;
import net.ryanland.colossus.bot.events.ButtonEvent;
import net.ryanland.colossus.bot.events.OnSlashCommandEvent;
import net.ryanland.colossus.util.file.Config;
import net.ryanland.colossus.util.file.database.DatabaseDriver;
import net.ryanland.colossus.util.file.local.LocalFile;
import net.ryanland.colossus.util.file.local.LocalFileBuilder;
import net.ryanland.colossus.util.file.local.LocalFileType;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A helper class for making a {@link Colossus} bot ready for startup
 *
 * @since 1.0
 * @author RyanLandDev
 */
public class ColossusBuilder {

    private static final Object[] INTERNAL_EVENTS = new ListenerAdapter[]{
        new ButtonEvent(), new OnSlashCommandEvent()
    };

    private JDABuilder jdaBuilder;
    private final Config config;
    private final List<Command> commands = new ArrayList<>();
    private final List<LocalFile> localFiles = new ArrayList<>();

    private boolean disableHelp = false;
    private DatabaseDriver databaseDriver = null;

    /**
     * Helper class to build a new instance of {@link Colossus}.<br>
     * @param configDirectory The directory the config.json file should be in, containing the bot token among other things.<br>
     *                        For example, a valid input could be: "src/config".<br>
     *                        This directory should be created manually before running your bot. When running, a config file<br>
     *                        will be automatically generated with empty fields for you to fill in.<br><br>
     *
     *                        <strong>WARNING:</strong> It is recommended to {@code .gitignore} your config.json to prevent
     *                        your bot token getting in hands of the wrong people.
     */
    public ColossusBuilder(String configDirectory) {
        LocalFile dir = new LocalFile(configDirectory);
        if (!dir.exists())
            throw new InvalidPathException(configDirectory, "This path is invalid or does not exist.");
        if (!dir.isDirectory())
            throw new InvalidPathException(configDirectory, "The provided path is not a directory.");

        LocalFile configFile = new LocalFileBuilder()
            .setName("/" + configDirectory + "/config")
            .setFileType(LocalFileType.JSON)
            .setDefaultContent("""
            {
              "token": "",
              "client_id": "",
              "permissions": "",
              
              "support_guild": "",
              "test_guild": "",
              "testing": true
            }""")
            .buildFile();

        JsonObject configJson = new JsonObject();
        try {
            configJson = configFile.parseJson();
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = new Config(configJson);
        jdaBuilder = JDABuilder.createDefault(config.getToken())
            .addEventListeners(INTERNAL_EVENTS);
    }

    /**
     * Helper class to build a new instance of {@link Colossus}.
     * <br>Note: using this constructor will let the bot ignore your {@code config.json} file.
     * <br>To use the config file instead, take advantage of the {@link ColossusBuilder#ColossusBuilder(String)} constructor.
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
     *
     * @param clientId The client ID of the bot.<br><br>
     *
     *                 Your bot's client ID can be retrieved by:<br>
     *                 - Go to <a href="https://discord.com/developers/applications">Discord Developer Applications</a><br>
     *                 - Click the bot you want to use<br>
     *                 - Under <i>Application ID</i>, click {@code Copy}<br>
     *                 - Paste it here<br><br>
     *
     * @param testGuild The ID of the Discord server you are testing your bot in.<br><br>
     *
     *                  Your server's ID can be retrieved by:<br>
     *                  - Open Discord<br>
     *                  - Enable Developer Mode (User settings > Advanced > Developer Mode)<br>
     *                  - Right-click your server and click {@code Copy ID}<br>
     *                  - Paste it here<br>
     */
    public ColossusBuilder(String token, String clientId, String testGuild) {
        config = new Config(token, clientId, testGuild);
        jdaBuilder = JDABuilder.createDefault(config.getToken())
            .addEventListeners(INTERNAL_EVENTS);
    }

    /**
     * Build the {@link Colossus} bot.
     * Note: to initialize the bot, use {@link Colossus#initialize()} after building
     * @return The built {@link Colossus} instance
     */
    public Colossus build() {
        if (!disableHelp) commands.add(new HelpCommand());

        return new Colossus(jdaBuilder, config, commands, localFiles, databaseDriver);
    }

    /**
     * Register {@link LocalFile}s
     * @param localFiles The file(s) to register
     * @return The builder
     * @see Colossus#getLocalFile(String)
     */
    public ColossusBuilder registerLocalFiles(LocalFile... localFiles) {
        this.localFiles.addAll(List.of(localFiles));
        return this;
    }

    /**
     * Register {@link Command}s
     * @param commands The command(s) to register
     * @return The builder
     */
    public ColossusBuilder registerCommands(Command... commands) {
        this.commands.addAll(List.of(commands));
        return this;
    }

    /**
     * Disables the default help command, allowing you to create your own. This command is enabled by default.
     * @return The builder
     */
    public ColossusBuilder disableHelp() {
        disableHelp = true;
        return this;
    }

    /**
     * Modify the {@link JDABuilder} implementation used for this bot
     * @param modifier The function to use, with the currently defined {@link JDABuilder} given
     * @return This {@link ColossusBuilder}
     */
    public ColossusBuilder setJDABuilder(Function<JDABuilder, JDABuilder> modifier) {
        jdaBuilder = modifier.apply(jdaBuilder);
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

}
