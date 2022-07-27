package net.ryanland.colossus.command.executor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.context.ContextCommandBuilder;
import net.ryanland.colossus.command.context.ContextCommandType;
import net.ryanland.colossus.command.regular.CommandBuilder;
import net.ryanland.colossus.command.regular.SlashCommand;
import net.ryanland.colossus.command.regular.SubCommand;
import net.ryanland.colossus.command.regular.SubCommandHolder;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.events.command.ContextCommandEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CommandHandler {

    private static final List<Command> COMMANDS = new ArrayList<>();
    private static final HashMap<String, Command> COMMAND_MAP = new HashMap<>();

    private static final List<ContextCommand<?>> CONTEXT_COMMANDS = new ArrayList<>();
    private static final HashMap<String, ContextCommand<User>> USER_CONTEXT_COMMAND_MAP = new HashMap<>();
    private static final HashMap<String, ContextCommand<Message>> MESSAGE_CONTEXT_COMMAND_MAP = new HashMap<>();

    private static final CommandExecutor COMMAND_EXECUTOR = new CommandExecutor();

    private static void commandError(Command command, String error) {
        throw new IllegalArgumentException(
            Objects.requireNonNullElse(command.getName(), command.getClass().getName()) + " - " + error);
    }

    public static void registerCommands(List<Command> commands) {
        for (Command command : commands) {
            // Check if the CommandBuilder annotation is present
            if (!command.getClass().isAnnotationPresent(CommandBuilder.class))
                commandError(command, "Commands must implement the CommandBuilder annotation.");
            // Check if the command's data is not null
            if (command.getName() == null || command.getDescription() == null || command.getCategory() == null)
                commandError(command, "Commands must have at least a name, description and category.");
            // Check if the command('s name) has already been registered
            if (COMMAND_MAP.containsKey(command.getName()))
                commandError(command, "A command with this name has already been registered.");
            if (COMMAND_MAP.containsValue(command))
                commandError(command, "This command has already been registered.");
            if (command instanceof SubCommand) {
                Colossus.LOGGER.warn(command.getName() + " - A subcommand has been registered as a command.");
                if (command instanceof SubCommandHolder)
                    commandError(command, "Nested subcommand holders may not be registered.");
            }

            // Add data
            COMMANDS.add(command);
            COMMAND_MAP.put(command.getName(), command);
        }
    }

    private static void commandError(ContextCommand<?> contextCommand, String error) {
        throw new IllegalArgumentException(
            Objects.requireNonNullElse(contextCommand.getName(), contextCommand.getClass().getName()) + " - " + error);
    }

    @SuppressWarnings("all")
    public static void registerContextCommands(List<ContextCommand<?>> contextCommands) {
        for (ContextCommand<?> contextCommand : contextCommands) {
            // Check if the ContextCommandBuilder annotation is present
            if (!contextCommand.getClass().isAnnotationPresent(ContextCommandBuilder.class))
                commandError(contextCommand, "Context commands must implement the ContextCommandBuilder annotation.");
            // Check if the command('s name) has already been registered && context type is not the same
            if (USER_CONTEXT_COMMAND_MAP.containsKey(contextCommand.getName()) &&
                USER_CONTEXT_COMMAND_MAP.get(contextCommand.getName()).getType() == contextCommand.getType())
                commandError(contextCommand, "A command with this name and type has already been registered.");
            if (USER_CONTEXT_COMMAND_MAP.containsValue(contextCommand))
                commandError(contextCommand, "This command has already been registered.");

            // Add data
            CONTEXT_COMMANDS.add(contextCommand);
            if (contextCommand.getType() == ContextCommandType.MESSAGE)
                MESSAGE_CONTEXT_COMMAND_MAP.put(contextCommand.getName(), (ContextCommand<Message>) contextCommand);
            else if (contextCommand.getType() == ContextCommandType.USER)
                USER_CONTEXT_COMMAND_MAP.put(contextCommand.getName(), (ContextCommand<User>) contextCommand);
        }
    }

    public static void upsertAll() {
        Guild testGuild = Colossus.getJDA().getGuildById(Colossus.getConfig().getTestGuildId());
        if (testGuild == null)
            throw new IllegalStateException("The bot is not in the provided test guild, or the ID is invalid.");

        // remove commands that were previously registered but not anymore
        Colossus.getJDA().updateCommands().queue();

        // normal commands
        for (Command command : COMMANDS) {
            if (!(command instanceof SlashCommand)) {
                if (!(command instanceof SubCommandHolder) ||
                    ((SubCommandHolder) command).getRealSubCommands().stream()
                        .noneMatch(subcommand -> subcommand instanceof SlashCommand))
                    continue;
            }

            SlashCommandData slashCmdData = Commands.slash(command.getName(), command.getDescription())
                .setLocalizationFunction(command.getLocalizationFunction())
                .setGuildOnly(command.isGuildOnly())
                .setDefaultPermissions(command.getDefaultPermissions());

            // Subcommands
            if (command instanceof SubCommandHolder) {
                for (SubCommand subcommand : command.getSubCommands()) {
                    if (subcommand instanceof SubCommandHolder) {
                        slashCmdData.addSubcommandGroups(((SubCommandHolder) subcommand).getSlashCommandData());
                    } else if (subcommand instanceof SlashCommand) {
                        slashCmdData.addSubcommands(subcommand.getSlashData());
                    }
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

        // Context commands
        for (ContextCommand<?> contextCommand : CONTEXT_COMMANDS) {
            CommandData cmdData = Commands.context(contextCommand.getType().getJDAEquivalent(), contextCommand.getName())
                .setLocalizationFunction(contextCommand.getLocalizationFunction())
                .setGuildOnly(contextCommand.isGuildOnly())
                .setDefaultPermissions(contextCommand.getDefaultPermissions());

            if (Colossus.getConfig().isTesting()) {
                testGuild.upsertCommand(cmdData).queue();
            } else {
                Colossus.getJDA().upsertCommand(cmdData).queue();
            }
        }
    }

    public static void handleAutocompleteEvent(CommandAutoCompleteInteractionEvent event) {
        Command command = getCommand(event.getName());
        Argument<?> argument = command.getArguments().get(event.getFocusedOption().getName());
        ArgumentOptionData optionData = argument.getArgumentOptionData();
        if (optionData.isAutoComplete()) {
            String text = event.getFocusedOption().getValue();
            List<Choice> choices = optionData.getAutoCompletableChoices()
                .stream().filter(choice -> choice.getName().startsWith(text))
                .limit(OptionData.MAX_CHOICES)
                .toList();
            event.replyChoices(choices).queue();
        }
    }

    public static List<BasicCommand> getAllCommands() {
        return Stream.concat(getCommands().stream(), getContextCommands().stream()).toList();
    }

    public static List<Command> getCommands() {
        return COMMANDS;
    }

    public static List<ContextCommand<User>> getUserContextCommands() {
        return new ArrayList<>(USER_CONTEXT_COMMAND_MAP.values());
    }

    public static List<ContextCommand<Message>> getMessageContextCommands() {
        return new ArrayList<>(MESSAGE_CONTEXT_COMMAND_MAP.values());
    }

    public static Command getCommand(String alias) {
        return COMMAND_MAP.get(alias);
    }

    public static List<ContextCommand<?>> getContextCommands() {
        return CONTEXT_COMMANDS;
    }

    @SuppressWarnings("all")
    public static ContextCommand<User> getUserContextCommand(String alias) {
        return USER_CONTEXT_COMMAND_MAP.get(alias);
    }

    @SuppressWarnings("all")
    public static ContextCommand<Message> getMessageContextCommand(String alias) {
        return MESSAGE_CONTEXT_COMMAND_MAP.get(alias);
    }

    public static ContextCommand<?> getContextCommand(ContextCommandType type, String alias) {
        if (type == ContextCommandType.MESSAGE) return getMessageContextCommand(alias);
        else if (type == ContextCommandType.USER) return getUserContextCommand(alias);
        return null;
    }

    public static void run(CommandEvent event) {
        COMMAND_EXECUTOR.run(event);
    }

    public static <T extends ISnowflake> void run(ContextCommandEvent<T> event) {
        COMMAND_EXECUTOR.run(event);
    }

    public static void execute(CommandEvent event) {
        COMMAND_EXECUTOR.execute(event);
    }

    public static <T extends ISnowflake> void execute(ContextCommandEvent<T> event) {
        COMMAND_EXECUTOR.execute(event);
    }
}
