package net.ryanland.colossus.command.executor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SlashCommand;
import net.ryanland.colossus.command.SubCommand;
import net.ryanland.colossus.command.SubCommandHolder;
import net.ryanland.colossus.events.CommandEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CommandHandler {

    private static final List<Command> COMMANDS = new ArrayList<>();
    private static final HashMap<String, Command> COMMAND_MAP = new HashMap<>();
    private static final CommandExecutor COMMAND_EXECUTOR = new CommandExecutor();

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
        }
    }

    public static void upsertAll() {
        Guild testGuild = Colossus.getJDA().getGuildById(Colossus.getConfig().getTestGuildId());
        if (testGuild == null)
            throw new IllegalStateException("The bot is not in the provided test guild, or the ID is invalid.");

        // remove commands that were previously registered but not anymore
        Colossus.getJDA().updateCommands().queue();

        for (Command command : COMMANDS) {
            if (!(command instanceof SlashCommand)) {
                if (!(command instanceof SubCommandHolder) ||
                    ((SubCommandHolder) command).getRealSubCommands().stream()
                        .noneMatch(subcommand -> subcommand instanceof SlashCommand))
                    continue;
            }

            SlashCommandData slashCmdData = Commands.slash(command.getName(), command.getDescription());

            // Subcommands
            if (command instanceof SubCommandHolder) {
                for (SubCommand subcommand : command.getSubCommands()) {
                    if (subcommand instanceof SubCommandHolder)
                        slashCmdData.addSubcommandGroups(((SubCommandHolder) subcommand).getSlashCommandData());
                    else if (subcommand instanceof SlashCommand)
                        slashCmdData.addSubcommands(subcommand.getSlashData());
                }
            // Regular commands
            } else {
                slashCmdData.addOptions(command.getOptionsData());
            }

            if (Colossus.getConfig().isTesting()) {
                testGuild.upsertCommand(slashCmdData).queue();
            } else {
                Colossus.getJDA().upsertCommand(slashCmdData).queue();
            }

        }
    }

    public static List<Command> getCommands() {
        return COMMANDS;
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
