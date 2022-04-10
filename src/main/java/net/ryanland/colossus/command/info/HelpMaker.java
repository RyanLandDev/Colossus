package net.ryanland.colossus.command.info;

import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SubCommand;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.impl.TestSubCommand;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayList;
import java.util.List;

public class HelpMaker {

    public static List<String> formattedUsageElements(Command command, Argument<?> highlighted) {
        List<String> elements = new ArrayList<>();
        elements.add("/" + command.getName());

        if (command instanceof SubCommand) {}
            //TODO elements.add(event.getSubCommandName());

        ArgumentSet arguments = command.getArguments();
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

        return elements;
    }

    public static List<String> formattedUsageElements(CommandEvent event) {
        return formattedUsageElements(event.getCommand(), null);
    }

    public static String formattedUsage(Command command, Argument<?> highlighted) {
        return String.join(" ", formattedUsageElements(command, highlighted));
    }

    public static String formattedUsage(Command command) {
        return formattedUsage(command, null);
    }

    public static String formattedUsageCode(CommandEvent event) {
        return "`" + formattedUsage(event.getCommand()) + "`";
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

    public static PresetBuilder commandEmbed(Command command) {
        PresetBuilder embed = new PresetBuilder()
            .setTitle(command.getUppercaseName() + " Command" +
                (command.isDisabled() ? " [Disabled]" : ""))
            .setDescription(command.getDescription() + "\n\u200b")
            .addLogo()
            .addField("Category", command.getCategory().name())
            .addField("Usage", String.format("```html\n%s\n```", formattedUsage(command)));

        if (command.getPermission() != null && !command.getPermission().isEmpty())
            embed.addField("Permission", command.getPermission().getName());

        return embed;
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
