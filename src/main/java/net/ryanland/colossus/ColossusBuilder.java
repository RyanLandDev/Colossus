package net.ryanland.colossus;

import net.dv8tion.jda.api.JDABuilder;
import net.ryanland.colossus.bot.command.impl.Command;
import net.ryanland.colossus.bot.command.impl.info.HelpCommand;
import net.ryanland.colossus.bot.events.ButtonEvent;
import net.ryanland.colossus.bot.events.OnSlashCommandEvent;
import net.ryanland.colossus.sys.file.Config;
import net.ryanland.colossus.sys.file.local.LocalFile;
import net.ryanland.colossus.sys.file.local.LocalFileBuilder;
import net.ryanland.colossus.sys.file.local.LocalFileType;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for making a {@link Colossus} bot ready for startup
 *
 * @since 1.0
 * @author RyanLandDev
 */
public class ColossusBuilder {

    private final JDABuilder jdaBuilder;
    private final Config config;
    private final List<Command> commands = new ArrayList<>();
    private final List<LocalFile> localFiles = new ArrayList<>();

    private boolean disableHelp = false;

    /**
     * Helper class to build a new instance of {@link Colossus}
     * @param configDirectory The directory the "config.json" file should be in, containing the bot token among other things.
     *                        For example, a valid input could be: "src/config"
     * @throws IOException If the provided path is not a directory or is invalid
     */
    public ColossusBuilder(String configDirectory) throws IOException {
        LocalFile dir = new LocalFile(configDirectory);
        if (!dir.isDirectory())
            throw new InvalidPathException(configDirectory, "This is not a valid path or directory.");

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

        config = new Config(configFile.parseJson());
        jdaBuilder = JDABuilder.createDefault(config.getToken())
            .addEventListeners(new ButtonEvent(), new OnSlashCommandEvent());
    }

    /**
     * Build the {@link Colossus} bot.
     * Note: to initialize the bot, use {@link Colossus#initialize()} after building
     * @return The built {@link Colossus} instance
     */
    public Colossus build() {
        if (!disableHelp) commands.add(new HelpCommand());

        return new Colossus(jdaBuilder, config, commands, localFiles);
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

}
