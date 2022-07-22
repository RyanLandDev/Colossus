package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import net.ryanland.colossus.events.CommandEvent;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class EnumArgument<E extends Enum<E> & EnumArgument.InputEnum> extends ArgumentStringResolver<E> {

    private final EnumSet<E> associatedEnum;

    public EnumArgument(Class<E> anEnum) {
        associatedEnum = EnumSet.allOf(anEnum);
    }

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return (ArgumentOptionData) super.getArgumentOptionData().addChoices(
            associatedEnum.stream()
                .filter(e -> !e.isHidden())
                .map(e -> new Command.Choice(e.getTitle(), e.getTitle()))
                .toArray(Command.Choice[]::new)
        );
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
