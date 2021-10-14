package net.ryanland.colossus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.internal.utils.JDALogger;
import net.ryanland.colossus.bot.command.executor.CommandHandler;
import net.ryanland.colossus.bot.command.impl.Command;
import net.ryanland.colossus.util.file.Config;
import net.ryanland.colossus.util.file.database.DatabaseDriver;
import net.ryanland.colossus.util.file.database.old.DocumentCache;
import net.ryanland.colossus.util.file.database.old.GlobalDocument;
import net.ryanland.colossus.util.file.local.LocalFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main class for initializing Colossus
 *
 * @since 1.0
 * @author RyanLandDev
 */
public class Colossus {

    private static final Logger LOGGER =
        JDALogger.getLog(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass());

    private static JDA jda;
    private static Config config;
    private static List<Command> commands;
    private static List<LocalFile> localFiles;
    private static DatabaseDriver databaseDriver;

    private final JDABuilder builder;

    public Colossus(JDABuilder builder, Config config,
                    List<Command> commands, List<LocalFile> localFiles,
                    DatabaseDriver databaseDriver) {
        this.builder = builder;
        Colossus.config = config;
        Colossus.commands = commands;
        Colossus.localFiles = localFiles;
        Colossus.databaseDriver = databaseDriver;
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

    public static Config getConfig() {
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
            return getLocalFiles().stream()
                .filter(file -> file.getName().equals(name))
                .collect(Collectors.toList())
                .get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidPathException(name, "This local file does not exist or is not registered.");
        }
    }

    /**
     * Get the configured {@link DatabaseDriver}.
     * @see ColossusBuilder#setDatabaseDriver(DatabaseDriver)
     * @see Colossus#Colossus(JDABuilder, Config, List, List, DatabaseDriver)
     */
    public static DatabaseDriver getDatabaseDriver() {
        if (databaseDriver == null)
            throw new IllegalStateException("A database driver has not been defined.");
        return databaseDriver;
    }
}
