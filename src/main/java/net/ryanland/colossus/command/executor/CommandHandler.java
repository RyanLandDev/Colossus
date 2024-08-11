package net.ryanland.colossus.command.executor;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.CommandException;
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
import net.ryanland.colossus.events.repliable.RepliableEvent;
import net.ryanland.colossus.sys.file.config.Config;
import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;

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
        if (!Config.getBoolean("slash_commands.enabled")) return;

        boolean global = Config.getBoolean("slash_commands.global");
        String guildId = Config.getString("slash_commands.guild_id");
        if (guildId.isEmpty() && !global) throw new NullPointerException("The slash_commands.guild_id config property may not be empty");
        Guild privateGuild = Colossus.getJDA().getGuildById(guildId);

        if (!global && privateGuild == null) {
            Colossus.LOGGER.error("The bot is not a member of the test guild defined in the configuration, or the ID is invalid.\n" +
                "Invite the bot to your server using this link: " + Colossus.getJDA().getInviteUrl(Permission.ADMINISTRATOR));
            System.exit(0);
        }

        // set updater
        CommandListUpdateAction updater = global ? Colossus.getJDA().updateCommands() : privateGuild.updateCommands();

        // normal commands
        for (Command command : COMMANDS) {
            if (!(command instanceof SlashCommand)) {
                if (!(command instanceof SubCommandHolder) ||
                    ((SubCommandHolder) command).getRealSubCommands().stream()
                        .noneMatch(subcommand -> subcommand instanceof SlashCommand))
                    continue;
            }

            if (!command.getClass().isAnnotationPresent(CommandBuilder.class)) {
                Colossus.LOGGER.error("The command " + command.getClass().getName() + " was not loaded, because it was not annotated with @CommandBuilder");
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

            updater.addCommands(slashCmdData);
        }

        // Context commands
        for (ContextCommand<?> contextCommand : CONTEXT_COMMANDS) {
            CommandData cmdData = Commands.context(contextCommand.getType().getJDAEquivalent(), contextCommand.getName())
                .setLocalizationFunction(contextCommand.getLocalizationFunction())
                .setGuildOnly(contextCommand.isGuildOnly())
                .setDefaultPermissions(contextCommand.getDefaultPermissions());

            updater.addCommands(cmdData);
        }

        // upsert
        updater.queue(commands -> commands.forEach(command -> COMMAND_MAP.get(command.getName()).setId(command.getId())));
    }

    public static void handleAutocompleteEvent(CommandAutoCompleteInteractionEvent event) {
        Command command = getCommand(event.getName());//This will get the "Command" of the first word in the slash command, e.g. "/test sub" -> "test"
        if (command instanceof SubCommandHolder) {//If this command is a subcommand holder, get the subcommand executed
            command = (Command) ((SubCommandHolder) command).getRealSubCommands().stream()
                .filter(cmd -> ((Command) cmd).getName().equals(event.getSubcommandName())).toList().get(0);
        }
        Argument<?> argument = command.getArguments().get(event.getFocusedOption().getName());
        ArgumentOptionData optionData = argument.getArgumentOptionData();
        if (optionData.isAutoComplete()) {
            optionData.getAutocompleteConsumer().accept(event, argument);
        }
    }

    public static void handleCommandException(Exception exception, RepliableEvent event) {
        if (!(exception instanceof CommandException)) {
            exception.printStackTrace();
        }

        event.reply(new PresetBuilder(Colossus.getErrorPresetType(),
            exception instanceof CommandException ?
                exception.getMessage() :
                "Unknown error, please report it to a developer."
        ));
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
