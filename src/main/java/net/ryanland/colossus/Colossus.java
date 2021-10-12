package net.ryanland.colossus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.ryanland.colossus.bot.command.executor.CommandHandler;
import net.ryanland.colossus.bot.command.impl.Command;
import net.ryanland.colossus.sys.file.Config;
import net.ryanland.colossus.sys.file.database.DocumentCache;
import net.ryanland.colossus.sys.file.database.documents.impl.GlobalDocument;
import net.ryanland.colossus.sys.file.local.LocalFile;

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

    private static JDA jda;
    private static Config config;
    private static List<Command> commands;
    private static List<LocalFile> localFiles;

    private final JDABuilder builder;

    public Colossus(JDABuilder builder, Config config, List<Command> commands, List<LocalFile> localFiles) {
        this.builder = builder;
        Colossus.config = config;
        Colossus.commands = commands;
        Colossus.localFiles = localFiles;
    }

    public void initialize() throws LoginException, InterruptedException {

        // Register commands
        CommandHandler.register(commands);

        // Build the bot
        jda = builder.build();
        jda.awaitReady();

        // Upsert the registered slash commands
        CommandHandler.upsertAll();
    }

    // Utility methods ------------------------------

    public static JDA getJda() {
        return jda;
    }

    public static SelfUser getSelfUser() {
        return jda.getSelfUser();
    }

    public static Config getConfig() {
        return config;
    }

    public static List<LocalFile> getLocalFiles() {
        return localFiles;
    }

    /**
     * Get one of the registered {@link LocalFile}s
     *
     * @param name The name of the {@link LocalFile} to get
     * @return The associated local file
     * @throws InvalidPathException If this file does not exist or is not registered
     * @see ColossusBuilder
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

    public static GlobalDocument getGlobalDocument() {
        return DocumentCache.getGlobal();
    }
}
