package dev.ryanland.colossus.command;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.ColossusBuilder;
import dev.ryanland.colossus.command.impl.DefaultHelpCommand;
import dev.ryanland.colossus.sys.util.Node;

/**
 * Represents a command category
 * @see Command
 * @see BaseCommand
 */
public class Category extends Node<Category> {

    private final String name;
    private final String description;
    private final String emoji;

    /**
     * Create a command category.<br>
     * To add subcategories, use the #addChildren(Category...) method.
     * @param name The name of the category
     * @param description The description of the category
     * @param emoji An emoji representing the category
     * @see Node#addChildren(Node[])
     * @see ColossusBuilder#registerCategories(Category...)
     * @see DefaultHelpCommand
     */
    public Category(String name, String description, String emoji) {
        this.name = name;
        this.description = description;
        this.emoji = emoji;
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

    public static Category of(String name) {
        try {
            return Colossus.getCategories().stream()
                .filter(category -> category.getName().equalsIgnoreCase(name)).toList().get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("The category " + name + " does not exist.");
        }
    }
}
