package net.ryanland.colossus.command.arguments.old.types.impl;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.old.types.SingleArgument;
import net.ryanland.colossus.events.MessageCommandEvent;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class EnumArgument<E extends Enum<E> & EnumArgument.InputEnum> extends SingleArgument<E> {

    private final EnumSet<E> associatedEnum;

    public EnumArgument(Class<E> anEnum) {
        associatedEnum = EnumSet.allOf(anEnum);
    }

    @Override
    public Command.Choice[] getChoices() {
        return associatedEnum.stream()
            .filter(e -> !e.isHidden())
            .map(e -> new Command.Choice(e.getTitle(), e.getTitle()))
            .toArray(Command.Choice[]::new);
    }

    @Override
    public E parsed(OptionMapping argument, MessageCommandEvent event) throws ArgumentException {
        for (E e : associatedEnum) {
            if (argument.getAsString().equals(e.getTitle())) {
                return e;
            }
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
