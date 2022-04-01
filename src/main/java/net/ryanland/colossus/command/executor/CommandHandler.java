package net.ryanland.colossus.command.executor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.*;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.info.HelpMaker;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

import java.util.*;
import java.util.function.BiConsumer;

public class CommandHandler {

    private static final List<Command> COMMANDS = new ArrayList<>();
    private static final HashMap<String, Command> COMMAND_MAP = new HashMap<>();
    private static final CommandExecutor COMMAND_EXECUTOR = new CommandExecutor();
    private static final Set<Category> CATEGORIES = new HashSet<>();

    private static void commandError(Command command, String error) {
        throw new IllegalArgumentException(
            Objects.requireNonNullElse(command.getName(), command.getClass().getName()) + " - " + error);
    }

    public static void register(List<Command> commands) {
        for (Command command : commands) {
            // Check if the command's data is not null
            if (command.getName() == null || command.getDescription() == null || command.getCategory() == null)
                commandError(command, "Commands must have at least a name, description and category.");
            // Check if the command('s name) has already been registered
            if (COMMAND_MAP.containsKey(command.getName()))
                commandError(command, "A command with this name has already been registered.");
            if (COMMAND_MAP.containsValue(command))
                commandError(command, "This command has already been registered.");
            if (command instanceof SubCommand) {
                Colossus.getLogger().warn(command.getName() + " - A subcommand has been registered as a command.");
                if (command instanceof SubCommandHolder)
                    commandError(command, "Nested subcommand holders may not be registered.");
            }

            // Add data
            COMMANDS.add(command);
            COMMAND_MAP.put(command.getName(), command);
            CATEGORIES.add(command.getCategory());
        }
    }

    public static void upsertAll() {
        Guild testGuild = Colossus.getJda().getGuildById(Colossus.getConfig().getTestGuildId());
        if (testGuild == null)
            throw new IllegalStateException("The bot is not in the provided test guild, or the ID is invalid.");

        for (Command command : COMMANDS) {
            if (!(command instanceof SlashCommand)) continue;

            CommandBuilder cmdInfo = HelpMaker.getInfo(command);
            CommandData slashCmdData = new CommandData(command.getName(), command.getDescription());

            // Subcommands
            if (command instanceof SubCommandHolder) {
                for (SubCommand subcommand : ((SubCommandHolder) command).getSubCommands()) {
                    if (subcommand instanceof SubCommandHolder)
                        slashCmdData.addSubcommandGroups(((SubCommandHolder) subcommand).getData());
                    else
                        slashCmdData.addSubcommands(subcommand.getData());
                }
            // Regular commands
            } else {
                slashCmdData.addOptions(command.getOptionsData());
            }

            if (Colossus.getConfig().isTesting()) {
                testGuild.upsertCommand(slashCmdData).queue();
            } else {
                Colossus.getJda().upsertCommand(slashCmdData).queue();
            }

        }
    }

    public static List<Command> getCommands() {
        return COMMANDS;
    }

    public static Set<Category> getCategories() {
        return CATEGORIES;
    }

    public static Command getCommand(String alias) {
        return COMMAND_MAP.get(alias);
    }

    public static void run(CommandEvent event) {
        COMMAND_EXECUTOR.run(event);
    }

    public static void execute(CommandEvent event) {
        COMMAND_EXECUTOR.execute(event);
    }
}
