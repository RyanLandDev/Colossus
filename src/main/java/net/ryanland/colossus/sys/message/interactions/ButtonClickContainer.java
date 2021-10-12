package net.ryanland.colossus.sys.message.interactions;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.ryanland.colossus.bot.command.executor.functional_interface.CommandBiConsumer;
import net.ryanland.colossus.bot.command.executor.functional_interface.CommandConsumer;

import java.util.function.Function;

public record ButtonClickContainer(
    CommandBiConsumer<ButtonClickEvent, Object> onClick, Function<ButtonClickEvent, Object> value) {

    public ButtonClickContainer(CommandConsumer<ButtonClickEvent> onClick) {
        this((event, obj) -> onClick.consume(event), event -> null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(ButtonClickEvent event) {
        return (T) value.apply(event);
    }

}
