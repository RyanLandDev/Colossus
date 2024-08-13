package dev.ryanland.colossus.command.arguments.types;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import net.dv8tion.jda.api.interactions.commands.Command;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.events.command.CommandEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EnumArgument<E extends Enum<E> & EnumArgument.InputEnum> extends ArgumentStringResolver<E> {

    private final EnumSet<E> associatedEnum;

    public EnumArgument(Class<E> anEnum) {
        associatedEnum = EnumSet.allOf(anEnum);
    }

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        List<Command.Choice> choices = associatedEnum.stream()
            .filter(e -> !e.isHidden())
            .map(e -> new Command.Choice(e.getTitle(), e.getTitle()))
            .toList();
        if (choices.size() > 25) {
            return super.getArgumentOptionData().addAutoCompletableChoices(choices);
        } else {
            return super.getArgumentOptionData().addChoices(choices);
        }
    }

    @Override
    public E resolve(String arg, CommandEvent event) throws ArgumentException {
        for (E element : associatedEnum) {
            if (arg.equals(element.getTitle()))
                return element;
        }
        throw new MalformedArgumentException("This is not a valid option! Choose from: " + getFormattedOptions());
    }

    private String getFormattedOptions() {
        return "`" +
            associatedEnum.stream()
                .filter(e -> !e.isHidden())
                .map(InputEnum::getTitle)
                .collect(Collectors.joining("` `"))
            + "`";
    }

    public interface InputEnum {

        String getTitle();

        default boolean isHidden() {
            return false;
        }
    }
}
