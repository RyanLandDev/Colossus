package net.ryanland.colossus.command.arguments;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArgumentOptionData extends OptionData {

    private List<Command.Choice> autoCompletableChoices = new ArrayList<>();

    public ArgumentOptionData(@NotNull OptionType type) {
        super(type, "name", "description");
    }

    /**
     * Adds choices which can be auto-completed, with no limit
     */
    public ArgumentOptionData addAutoCompletableChoices(Collection<Command.Choice> choices) {
        setAutoComplete(true);
        autoCompletableChoices.addAll(choices);
        return this;
    }

    /**
     * Adds choices which can be auto-completed, with no limit
     */
    public ArgumentOptionData addAutoCompletableChoices(Command.Choice... choices) {
        return addAutoCompletableChoices(List.of(choices));
    }

    public ArgumentOptionData clearAutoCompletableChoices() {
        setAutoComplete(false);
        autoCompletableChoices = new ArrayList<>();
        return this;
    }

    public List<Command.Choice> getAutoCompletableChoices() {
        return autoCompletableChoices;
    }
}
