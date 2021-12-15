package net.ryanland.colossus.command.executor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SubCommand;
import net.ryanland.colossus.command.SubCommandHolder;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.Category;
import net.ryanland.colossus.command.info.GroupInfo;
import net.ryanland.colossus.command.info.HelpMaker;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

import java.util.*;
import java.util.function.BiConsumer;

public class CommandHandler {

    private static final List<Command> COMMANDS = new ArrayList<>();
    private static final HashMap<String, Command> COMMAND_MAP = new HashMap<>();
    private static final CommandExecutor COMMAND_EXECUTOR = new CommandExecutor();
    private static final Set<Category> CATEGORIES = new HashSet<>();

    public static void register(List<Command> commands) {
        for (Command command : commands) {
            // Check if the command's data is not null
            if (command.getName() == null || command.getDescription() == null || command.getCategory() == null)
                throw new IllegalArgumentException(command.getClass().getName() +
                    " - Commands must have at least a name, description and category.");
            // Check if the command('s name) has already been registered
            if (COMMAND_MAP.containsKey(command.getName()))
                throw new IllegalArgumentException(
                    command.getName() + " - A command with this name has already been registered.");
            if (COMMAND_MAP.containsValue(command))
                throw new IllegalArgumentException(command.getName() + " - This command has already been registered.");

            // Add data
            COMMANDS.add(command);
            COMMAND_MAP.put(command.getName(), command);
            CATEGORIES.add(command.getCategory());

            // Add subcommands if existent
            if (command instanceof SubCommandHolder)
                for (SubCommand subcommand : command.getSubCommands())
                    COMMAND_MAP.put(subcommand.getName(), subcommand);
        }
    }

    private static final BiConsumer<SubCommand, CommandData> UPSERT_CONSUMER = (command, data) -> {
        SubcommandData subCmdData = new SubcommandData(command.getName(), command.getDescription());
        for (Argument<?> arg : command.getArguments()) {
            subCmdData.addOptions(arg.getOptionData());
        }
        data.addSubcommands(subCmdData);

    };

    public static void upsertAll() {
        Guild testGuild = Colossus.getJda().getGuildById(Colossus.getConfig().getTestGuildId());
        if (testGuild == null) {
            throw new IllegalStateException("The bot is not in the provided test guild, or the ID is invalid.");
        }

        for (Command command : COMMANDS) {
            CommandBuilder cmdInfo = HelpMaker.getInfo(command);
            CommandData slashCmdData = new CommandData(command.getName(), command.getDescription());

            if (command.getSubCommands().isEmpty() && command.getSubCommandGroups().isEmpty()) {
                for (Argument<?> arg : command.getArguments()) {
                    slashCmdData.addOptions(arg.getOptionData());
                }

            } else if (command.getSubCommandGroups().isEmpty()) {
                for (SubCommand subCmd : command.getSubCommands()) {
                    UPSERT_CONSUMER.accept(subCmd, slashCmdData);
                }
            } else {
                for (GroupInfo group : command.getSubCommandGroups()) {
                    SubcommandGroupData subCmdGroupData = new SubcommandGroupData(group.getName(), group.getDescription());

                    for (SubCommand subCmd : group.getSubCommands()) {
                        UPSERT_CONSUMER.accept(subCmd, slashCmdData);
                    }
                    slashCmdData.addSubcommandGroups(subCmdGroupData);
                }
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

    public static void run(MessageCommandEvent event) {
        COMMAND_EXECUTOR.run(event);
    }

    public static void run(SlashEvent event) {
        COMMAND_EXECUTOR.run(event);
    }

    public static void execute(MessageCommandEvent event, List<OptionMapping> args) {
        COMMAND_EXECUTOR.execute(event);
    }
}
