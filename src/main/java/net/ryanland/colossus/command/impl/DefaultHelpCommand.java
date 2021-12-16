package net.ryanland.colossus.command.impl;

import net.ryanland.colossus.command.*;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.CommandArgument;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.info.HelpMaker;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.interactions.menu.TabMenuBuilder;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CommandBuilder(
        name = "help",
        description = "Get a list of all commands or information about a specific one.",
        guildOnly = false
)
public class DefaultHelpCommand extends DefaultCommand implements CombinedCommand {

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArguments(
            new CommandArgument()
                .id("command")
                .optional()
                .description("Command to get information of")
        );
    }

    @Override
    public void execute(CommandEvent event) throws CommandException {
        Command command = event.getArgument("command");
        if (command == null)
            supplyCommandList(event);
        else
            supplyCommandHelp(event, command);
    }

    private void supplyCommandList(CommandEvent event) throws CommandException {
        // Init menu
        TabMenuBuilder menu = new TabMenuBuilder();

        // Add home to menu
        PresetBuilder homePage = new PresetBuilder(
            "Use the buttons below to navigate through all command categories.\n" +
                "You can get help for a specific command using " + HelpMaker.formattedUsageCode(event)
                + ".")
            .addLogo();
        menu.addPage("Home", homePage, true);

        // Iterate over all command categories
        for (Category category : CommandHandler.getCategories()) {
            // Get all commands, and filter by category equal and player has sufficient permissions
            List<Command> commands = CommandHandler.getCommands().stream().filter(c ->
                c.getCategory().equals(category) && c.memberHasPermission(event.getMember())
            ).collect(Collectors.toList());

            // If no commands were left after the filter, do not include this category in the menu
            if (commands.size() == 0) continue;

            // Sort by name
            commands.sort(Comparator.comparing(Command::getName));

            // Add this category to the menu
            menu.addPage(category.name(), new PresetBuilder(category.description() +
                "\n\n" + HelpMaker.formattedQuickCommandList(commands))
                .addLogo(), category.emoji());
        }

        // Build and send the menu
        menu.build().send(event);
    }

    private void supplyCommandHelp(CommandEvent event, Command command) {
        event.reply(HelpMaker.commandEmbed(command));
    }
}
