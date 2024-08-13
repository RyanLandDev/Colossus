package dev.ryanland.colossus.command;

import dev.ryanland.colossus.command.impl.DefaultHelpCommand;
import dev.ryanland.colossus.command.regular.SubCommandHolder;
import dev.ryanland.colossus.ColossusBuilder;
import dev.ryanland.colossus.sys.util.Node;

import java.util.List;

/**
 * Represents a command category
 * @see Command
 * @see BaseCommand
 */
public class Category extends Node<Category> {

    private final String name;
    private final String description;
    private final String emoji;
    private final List<Command> commands;

    /**
     * Create a command category.<br>
     * To add subcategories, use the #addChildren(Category...) method.
     * @param name The name of the category
     * @param description The description of the category
     * @param emoji An emoji representing the category
     * @param commands Commands in this category
     * @see Node#addChildren(Node[])
     * @see ColossusBuilder#registerCategories(Category...)
     * @see Command
     * @see BaseCommand
     * @see DefaultHelpCommand
     */
    public Category(String name, String description, String emoji, Command... commands) {
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        for (Command command : commands) {
            command.setCategory(this);
            if (command instanceof SubCommandHolder) {
                command.getSubCommands().forEach(subcommand -> {
                    ((Command) subcommand).setCategory(this);
                    if (subcommand instanceof SubCommandHolder) {
                        ((Command) subcommand).getSubCommands()
                            .forEach(nestedSubcommand -> ((Command) nestedSubcommand).setCategory(this));
                    }
                });
            }
        }
        this.commands = List.of(commands);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
    }

    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Gets all commands in this category and its subcategories
     */
    public List<Command> getAllCommands() {
        return getNestedCommands(getCommands());
    }

    private List<Command> getNestedCommands(List<Command> collector) {
        for (Category category : this) {
            collector.addAll(category.getNestedCommands(collector));
        }
        return collector;
    }
}
