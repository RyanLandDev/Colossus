package net.ryanland.colossus.command.info;

import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SubCommand;
import net.ryanland.colossus.command.SubCommandHolder;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.impl.TestTwoCommand;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;
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
            elements.add(String.format("<%s>", ((SubCommandHolder) command).getSubCommands()
                .stream().map(subCommand -> ((Command) subCommand).getName()).collect(Collectors.joining("/"))));
            elements.add("[...]");
        } else {
            ArgumentSet arguments = command.getArguments();
            if (arguments != null) {
                for (Argument<?> argument : arguments) {
                    String usage = argument.getName();
                    if (argument.isOptional())
                        usage = String.format("[%s]", usage);
                    else
                        usage = String.format("<%s>", usage);

                    // highlight check
                    if (highlighted != null && highlighted.getId().equals(argument.getId()))
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

    public static CommandBuilder getInfo(Command cmd) {
        return cmd.getClass().getAnnotation(CommandBuilder.class);
    }
}
