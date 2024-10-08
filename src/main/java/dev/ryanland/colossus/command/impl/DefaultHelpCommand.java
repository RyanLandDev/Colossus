package dev.ryanland.colossus.command.impl;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.Category;
import dev.ryanland.colossus.command.Command;
import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.command.arguments.ArgumentSet;
import dev.ryanland.colossus.command.arguments.types.command.CommandArgument;
import dev.ryanland.colossus.command.executor.CommandHandler;
import dev.ryanland.colossus.command.info.HelpMaker;
import dev.ryanland.colossus.command.regular.CombinedCommand;
import dev.ryanland.colossus.command.regular.CommandBuilder;
import dev.ryanland.colossus.command.regular.SubCommand;
import dev.ryanland.colossus.command.regular.SubCommandHolder;
import dev.ryanland.colossus.events.command.BasicCommandEvent;
import dev.ryanland.colossus.events.command.CommandEvent;
import dev.ryanland.colossus.sys.interactions.menu.tab.TabMenuBuilder;
import dev.ryanland.colossus.sys.interactions.menu.tab.TabMenuPage;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;

import java.util.List;

@CommandBuilder(
    name = "help",
    description = "Get a list of all commands or information about a specific one.",
    category = "Default",
    guildOnly = false
)
public final class DefaultHelpCommand extends DefaultCommand implements CombinedCommand {

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArguments(
            new CommandArgument()
                .name("command")
                .description("Command to get information of")
                .optional()
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
            addCategoryPage(menu, null, category, event);
        }

        // send
        event.reply(menu.build());
    }

    private void addCategoryPage(TabMenuBuilder menu, TabMenuPage page, Category category, BasicCommandEvent event) {
        // get all commands, and filter by category equal and member has sufficient permissions
        //event.getGuild().retrieveCommandPrivileges().queue(privilegeConfig -> {
            List<Command> commands = CommandHandler.getCommands().stream().filter(c ->
                c.getCategory().equals(category) && c.getPermission().check(event)
            ).toList();

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
            for (Category subcategory : category) addCategoryPage(menu, categoryPage, subcategory, event);
        //});
    }

    private void supplyCommandHelp(CommandEvent event, Command command) throws CommandException {
        // base command
        PresetBuilder baseEmbed = generateCommandEmbed(event, command, null, null);

        // return base command info
        if (!(command instanceof SubCommandHolder)) {
            event.reply(baseEmbed);
        // if a subcommand holder is used, use a menu instead with buttons for the subcommands
        } else {
            TabMenuBuilder menu = new TabMenuBuilder().setHomePage(baseEmbed);
            for (SubCommand subcommand : command.getSubCommands()) {
                // for every subcommand, create a page
                TabMenuPage page = new TabMenuPage(((Command) subcommand).getName(),
                    generateCommandEmbed(event, (Command) subcommand, (SubCommandHolder) command, null),
                    null, false);

                // if this subcommand contains nested subcommands, create subpages for those
                if (subcommand instanceof SubCommandHolder) {
                    for (SubCommand nestedSubcommand : ((SubCommandHolder) subcommand).getSubCommands()) {
                        page.addChildren(new TabMenuPage(((Command) nestedSubcommand).getName(),
                            generateCommandEmbed(event, (Command) nestedSubcommand, (SubCommandHolder) command,
                                (SubCommandHolder) subcommand), null, false));
                    }
                }

                // add the page to the menu
                menu.addPages(page);
            }
            // send
            event.reply(menu.build());
        }
    }

    private PresetBuilder generateCommandEmbed(CommandEvent event, Command command,
                                               SubCommandHolder headSubCommandHolder, SubCommandHolder nestedSubCommandHolder) {
        PresetBuilder embed = new PresetBuilder()
            .setTitle(((Command) (headSubCommandHolder == null ? command : headSubCommandHolder)).getUppercaseName() + " Command" +
                (headSubCommandHolder == null && nestedSubCommandHolder == null ? "" : " - " +
                    (headSubCommandHolder == null || nestedSubCommandHolder == null ? command.getName() :
                        ((Command) nestedSubCommandHolder).getName() + " " + command.getName())
                ) + (command.isDisabled() ? " [Disabled]" : ""))
            .setDescription(command.getDescription() + "\n\u200b")
            .addLogo()
            .addField("Category", command.getCategory().getName())
            .addField("Usage", String.format("```html\n%s\n```",
                HelpMaker.formattedUsage(command, null,
                    event.getUsedPrefix(), headSubCommandHolder, nestedSubCommandHolder)
            ));

        if (command.getPermission() != null && !command.getPermission().isEmpty()) {
            embed.addField("Permission Required", command.getPermission().getName());
        }

        return embed;
    }
}
