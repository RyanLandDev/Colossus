package net.ryanland.colossus.command.executor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SubCommand;
import net.ryanland.colossus.command.SubCommandHolder;
import net.ryanland.colossus.command.info.CommandInfo;
import net.ryanland.colossus.command.info.SubCommandGroup;
import net.ryanland.colossus.events.ContentCommandEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class CommandHandler {

    private static final List<Command> COMMANDS = new ArrayList<>();
    private static final HashMap<String, Command> COMMAND_MAP = new HashMap<>();
    private static final CommandExecutor COMMAND_EXECUTOR = new CommandExecutor();

    public static void register(List<Command> commands) {
        for (Command command : commands) {
            if (command.getName() == null || command.getDescription() == null || command.getCategory() == null)
                throw new IllegalArgumentException(command.getClass().getName() +
                    " - Commands must have at least a name, description and category.");
            if (COMMAND_MAP.containsKey(command.getName()))
                throw new IllegalArgumentException(
                    command.getName() + " - A command with this name has already been registered.");
            if (COMMAND_MAP.containsValue(command))
                throw new IllegalArgumentException(command.getName() + " - This command has already been registered.");

            COMMANDS.add(command);
            COMMAND_MAP.put(command.getName(), command);

            if (command instanceof SubCommandHolder) {
                for (SubCommand subcommand : command.getSubCommands()) {
                    COMMAND_MAP.put(subcommand.getName(), subcommand);
                }
            }
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
            CommandInfo cmdInfo = command.getInfo();
            CommandData slashCmdData = new CommandData(cmdInfo.getName(), cmdInfo.getDescription());

            if (cmdInfo.getSubCommands().isEmpty() && cmdInfo.getSubCommandGroups().isEmpty()) {
                for (Argument<?> arg : command.getArguments()) {
                    slashCmdData.addOptions(arg.getOptionData());
                }

            } else if (cmdInfo.getSubCommandGroups().isEmpty()) {
                for (SubCommand subCmd : cmdInfo.getSubCommands()) {
                    UPSERT_CONSUMER.accept(subCmd, slashCmdData);
                }
            } else {
                for (SubCommandGroup group : cmdInfo.getSubCommandGroups()) {
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

    public static Command getCommand(String alias) {
        return COMMAND_MAP.get(alias);
    }

    public static void run(ContentCommandEvent event) {
        COMMAND_EXECUTOR.run(event);
    }

    public static void execute(ContentCommandEvent event, List<OptionMapping> args) {
        COMMAND_EXECUTOR.execute(event, args);
    }
}
