package net.ryanland.colossus.command.arguments;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class ArgumentOptionData extends OptionData {

    private List<Command.Choice> autoCompletableChoices = new ArrayList<>();
    private BiConsumer<CommandAutoCompleteInteractionEvent, Argument<?>> autocompleteConsumer = (event, arg) -> {
        String text = event.getFocusedOption().getValue();
        List<Command.Choice> choices = getAutoCompletableChoices()
            .stream().filter(choice -> choice.getName().startsWith(text))
            .limit(OptionData.MAX_CHOICES)
            .toList();
        event.replyChoices(choices).queue();
    };

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

    /**
     * Sets the code to execute when the autocomplete event is fired. Provides the autocomplete event and the current argument.<br>
     * By default, this will be a consumer that replies with choices starting with the currently inputted text.
     */
    public ArgumentOptionData setAutocompleteConsumer(BiConsumer<CommandAutoCompleteInteractionEvent, Argument<?>> consumer) {
        autocompleteConsumer = consumer;
        return this;
    }

    public ArgumentOptionData clearAutoCompletableChoices() {
        setAutoComplete(false);
        autoCompletableChoices = new ArrayList<>();
        return this;
    }

    public List<Command.Choice> getAutoCompletableChoices() {
        return autoCompletableChoices;
    }

    public BiConsumer<CommandAutoCompleteInteractionEvent, Argument<?>> getAutocompleteConsumer() {
        return autocompleteConsumer;
    }

    @NotNull
    @Override
    public ArgumentOptionData addChoice(@NotNull String name, double value) {
        return (ArgumentOptionData) super.addChoice(name, value);
    }

    @NotNull
    @Override
    public ArgumentOptionData addChoice(@NotNull String name, long value) {
        return (ArgumentOptionData) super.addChoice(name, value);
    }

    @NotNull
    @Override
    public ArgumentOptionData addChoice(@NotNull String name, @NotNull String value) {
        return (ArgumentOptionData) super.addChoice(name, value);
    }

    @NotNull
    @Override
    public ArgumentOptionData addChoices(@NotNull Command.Choice... choices) {
        return (ArgumentOptionData) super.addChoices(choices);
    }

    @NotNull
    @Override
    public ArgumentOptionData addChoices(@NotNull Collection<? extends Command.Choice> choices) {
        return (ArgumentOptionData) super.addChoices(choices);
    }
}
