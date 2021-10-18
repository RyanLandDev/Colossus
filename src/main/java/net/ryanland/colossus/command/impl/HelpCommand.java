package net.ryanland.colossus.command.impl;

import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.MessageCommand;
import net.ryanland.colossus.command.SlashCommand;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.impl.CommandArgument;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.info.Category;
import net.ryanland.colossus.command.info.HelpMaker;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.interactions.menu.TabMenuBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CommandBuilder(
    name = "help",
    description = "Get a list of all commands or information about a specific one.",
    category = Category.INFORMATION
)
public class HelpCommand extends Command implements SlashCommand, MessageCommand {

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArguments(
            new CommandArgument()
                .id("command")
                .optional()
                .description("Command to get information of")
        );
    }

    // Slash command implementation -------------------

    @Override
    public void run(SlashEvent event) {
        Command command = event.getArgument("command");

        if (command == null) {
            supplyCommandList(event);
        } else {
            supplyCommandHelp(event, command);
        }
    }

    private void supplyCommandList(SlashEvent event) {
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
                    event.getMember().hasPermission(c.getPermission())
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

    private void supplyCommandHelp(SlashEvent event, Command command) {
        event.performReply(HelpMaker.commandEmbed(event, command)).queue();
    }

    // Message command implementation -------------------

    @Override
    public void run(MessageCommandEvent event) throws CommandException {
        event.reply("todo");
        //TODO
    }
}
