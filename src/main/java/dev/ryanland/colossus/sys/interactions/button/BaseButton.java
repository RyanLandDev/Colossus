package dev.ryanland.colossus.sys.interactions.button;

import dev.ryanland.colossus.events.repliable.ButtonClickEvent;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import dev.ryanland.colossus.command.executor.functional_interface.CommandPredicate;

import java.util.List;

/**
 * A Colossus Button. Also see the static methods within this record for helper constructors.
 * @param button The JDA Button object
 * @param onClick What to do when this button is clicked, with the click event given
 */
public record BaseButton(Button button, CommandConsumer<ButtonClickEvent> onClick) {

    public BaseButton(Button button) {
        this(button, event -> {});
    }

    public BaseButton of(Button button) {
        return new BaseButton(button);
    }

    /**
     * Create a button which only works if the provided predicate is true, and do something if false
     */
    public static BaseButton predicate(CommandPredicate<ButtonClickEvent> predicate,
                                       CommandConsumer<ButtonClickEvent> ifFalse, Button button,
                                       CommandConsumer<ButtonClickEvent> onClick) {
        return new BaseButton(button, event -> {
            if (!predicate.test(event)) ifFalse.accept(event);
            else if (onClick != null) onClick.accept(event);
        });
    }

    /**
     * Create a button which only one user can press
     */
    public static BaseButton user(Long userId, Button button,
                                  CommandConsumer<ButtonClickEvent> onClick) {
        return group(new Long[]{ userId }, button, onClick);
    }

    /**
     * Create a button which only a specific group of users can press
     */
    public static BaseButton group(Long[] userIds, Button button,
                                   CommandConsumer<ButtonClickEvent> onClick) {
        return predicate(event -> List.of(userIds).contains(event.getUser().getIdLong()),
            event -> event.reply(new PresetBuilder(Colossus.getErrorPresetType())
                .setTitle("Not Allowed")
                .setDescription("You're not allowed to use this button.")
            ), button, onClick);
    }
}
