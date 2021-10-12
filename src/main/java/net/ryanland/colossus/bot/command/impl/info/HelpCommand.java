package net.ryanland.colossus.bot.command.impl.info;

import net.ryanland.colossus.bot.command.arguments.ArgumentSet;
import net.ryanland.colossus.bot.command.arguments.types.impl.CommandArgument;
import net.ryanland.colossus.bot.command.executor.CommandHandler;
import net.ryanland.colossus.bot.command.impl.Command;
import net.ryanland.colossus.bot.command.info.Category;
import net.ryanland.colossus.bot.command.info.CommandInfo;
import net.ryanland.colossus.bot.command.info.HelpMaker;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.util.message.builders.PresetBuilder;
import net.ryanland.colossus.util.message.interactions.menu.tab.TabMenuBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

    @Override
    public CommandInfo getInfo() {
        return new CommandInfo()
            .name("help")
            .description("Get a list of all commands or information about a specific one.")
            .category(Category.INFORMATION)
            .cooldown(3);
    }

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
    public void run(CommandEvent event) {
        Command command = event.getArgument("command");

        if (command == null) {
            supplyCommandList(event);
        } else {
            supplyCommandHelp(event, command);
        }
    }

    private void supplyCommandList(CommandEvent event) {
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
        for (Category category : Category.getCategories()) {
            // Get all commands, and filter by category equal and player has sufficient permissions
            List<Command> commands = CommandHandler.getCommands().stream().filter(c ->
                c.getCategory() == category &&
                    c.getPermission().hasPermission(event.getMember())
            ).collect(Collectors.toList());

            // If no commands were left after the filter, do not include this category in the menu
            if (commands.size() == 0) continue;

            // Sort by name
            commands.sort(Comparator.comparing(Command::getName));

            // Add this category to the menu
            menu.addPage(category.getName(), new PresetBuilder(category.getDescription() +
                "\n\n" + HelpMaker.formattedQuickCommandList(commands))
                .addLogo(), category.getEmoji());
        }

        // Build and send the menu
        menu.build().send(event.getInteraction());
    }

    private void supplyCommandHelp(CommandEvent event, Command command) {
        event.performReply(HelpMaker.commandEmbed(event, command)).queue();
    }
}
