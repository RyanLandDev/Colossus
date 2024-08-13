package dev.ryanland.colossus.command.info;

import dev.ryanland.colossus.command.Command;
import dev.ryanland.colossus.command.arguments.Argument;
import dev.ryanland.colossus.command.arguments.ArgumentSet;
import dev.ryanland.colossus.command.regular.SubCommand;
import dev.ryanland.colossus.command.regular.SubCommandHolder;
import dev.ryanland.colossus.events.command.CommandEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelpMaker {

    public static String formattedUsage(@NotNull CommandEvent event, @Nullable Argument<?> highlighted) {
        return formattedUsage(event.getCommand(), highlighted, event.getUsedPrefix(),
            event.getHeadSubCommandHolder(), event.getNestedSubCommandHolder());
    }

    public static String formattedUsage(@NotNull Command command, @Nullable Argument<?> highlighted, @NotNull String prefix,
                                        @Nullable SubCommandHolder headSubCommandHolder,
                                        @Nullable SubCommandHolder nestedSubCommandHolder) {
        List<String> elements = new ArrayList<>();

        if (command instanceof SubCommand) {
            if (headSubCommandHolder != null)
                elements.add(((Command) headSubCommandHolder).getName());
            if (nestedSubCommandHolder != null)
                elements.add(((Command) nestedSubCommandHolder).getName());
        }
        elements.add(command.getName());

        if (command instanceof SubCommandHolder) {
            elements.add(String.format("<%s>", command.getSubCommands()
                .stream().map(subCommand -> ((Command) subCommand).getName()).collect(Collectors.joining("/"))));
            elements.add("[...]");
        } else {
            ArgumentSet arguments = command.getArguments();
            if (arguments != null) {
                for (Argument<?> argument : arguments.values()) {
                    String usage = argument.getName();
                    if (argument.isOptional())
                        usage = String.format("[%s]", usage);
                    else
                        usage = String.format("<%s>", usage);

                    // highlight check
                    if (highlighted != null && highlighted.getName().equals(argument.getName()))
                        usage = String.format("**%s**", usage);

                    elements.add(usage);
                }
            }
        }

        return prefix + String.join(" ", elements);
    }

    public static String formattedUsageCode(CommandEvent event) {
        return "`" + formattedUsage(event, null) + "`";
    }

    public static String formattedSubCommands(SubCommand[] subcommands) {
        List<String> names = new ArrayList<>();
        for (SubCommand subcommand : subcommands)
            names.add(((Command) subcommand).getName());
        return "`" + String.join("`, `", names) + "`";
    }

    public static String formattedSubCommandsUsage(SubCommand[] subcommands) {
        List<String> names = new ArrayList<>();
        for (SubCommand subcommand : subcommands)
            names.add(((Command) subcommand).getName());
        return String.join("/", names);
    }

    public static String formattedQuickCommandList(List<Command> commands) {
        List<String> commandNames = new ArrayList<>();
        for (Command command : commands) {
            if (!command.isDisabled()) {
                commandNames.add(command.getName());
            }
        }
        return "`" + String.join("` `", commandNames) + "`";
    }
}
