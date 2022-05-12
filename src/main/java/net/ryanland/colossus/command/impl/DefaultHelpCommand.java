package net.ryanland.colossus.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.*;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.CommandArgument;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.info.HelpMaker;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.interactions.menu.TabMenuBuilder;
import net.ryanland.colossus.sys.interactions.menu.TabMenuPage;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CommandBuilder(
        name = "help",
        description = "Get a list of all commands or information about a specific one.",
        guildOnly = false
)
public final class DefaultHelpCommand extends DefaultCommand implements CombinedCommand {

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
        if (command == null) supplyCommandList(event);
        else supplyCommandHelp(event, command);
    }

    private void supplyCommandList(CommandEvent event) throws CommandException {
        // setup
        TabMenuBuilder menu = new TabMenuBuilder().setHomePage(
            new PresetBuilder("Help", "Use the buttons below to navigate through all command categories.\n" +
                "You can get help for a specific command using " + HelpMaker.formattedUsageCode(event) + ".")
            .addLogo()
        );

        // build the menu pages
        for (Category category : Colossus.getCategories()) {
            addCategoryPage(menu, null, category, event.getMember());
        }

        // send
        event.reply(menu.build());
    }

    private void addCategoryPage(TabMenuBuilder menu, TabMenuPage page, Category category, Member member) {
        // get all commands, and filter by category equal and member has sufficient permissions
        List<Command> commands = CommandHandler.getCommands().stream().filter(c ->
            c.getCategory().equals(category) && c.memberHasPermission(member)
        ).collect(Collectors.toList());

        // if no commands were left after the filter, do not include this category in the menu
        if (commands.isEmpty()) return;

        // create page object
        TabMenuPage categoryPage = new TabMenuPage(category.getName(), new PresetBuilder(category.getName(),
            category.getDescription() + "\n\n" + HelpMaker.formattedQuickCommandList(commands))
                .addLogo(), category.getEmoji(), false);

        // if page is null, this is a root category, so add it to the base menu
        // else, this is a subcategory, so add this page as a subpage to the parent category page
        if (page == null) menu.addPages(categoryPage);
        else page.addChildren(categoryPage);

        // add all subcategories and its subcategories (etc.) using recursion
        for (Category subcategory : category) addCategoryPage(menu, categoryPage, subcategory, member);
    }

    private void supplyCommandHelp(CommandEvent event, Command command) {
        // base command
        PresetBuilder baseEmbed = new PresetBuilder()
            .setTitle(command.getUppercaseName() + " Command" +
                (command.isDisabled() ? " [Disabled]" : ""))
            .setDescription(command.getDescription() + "\n\u200b")
            .addLogo()
            .addField("Category", command.getCategory().getName())
            .addField("Usage", String.format("```html\n%s\n```",
                HelpMaker.formattedUsage(command, null, event.getUsedPrefix(), null, null)));
        if (command.getPermission() != null && !command.getPermission().isEmpty())
            baseEmbed.addField("Permission Required", command.getPermission().getName());

        // return base command info
        if (!(command instanceof SubCommandHolder))
            event.reply(baseEmbed);
        // if a subcommand holder is used, use a menu instead with buttons for the subcommands
        else {

        }
    }
}
