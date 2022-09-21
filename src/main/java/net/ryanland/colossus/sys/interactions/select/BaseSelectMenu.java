package net.ryanland.colossus.sys.interactions.select;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.command.executor.functional_interface.CommandPredicate;
import net.ryanland.colossus.events.SelectMenuEvent;
import net.ryanland.colossus.sys.interactions.ComponentRow;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Collections;
import java.util.List;

public class BaseSelectMenu extends ComponentRow {

    private final SelectMenu selectMenu;
    private final CommandConsumer<SelectMenuEvent> onSubmit;

    /**
     * A Colossus Select Menu. Also see the static methods within this record for helper constructors.
     * @param selectMenu The JDA {@link SelectMenu} object
     * @param onSubmit What to do when this select menu is submitted, with the event given
     */
    public BaseSelectMenu(SelectMenu selectMenu, CommandConsumer<SelectMenuEvent> onSubmit) {
        this.selectMenu = selectMenu;
        this.onSubmit = onSubmit;
    }

    public SelectMenu getSelectMenu() {
        return selectMenu;
    }

    public CommandConsumer<SelectMenuEvent> getOnSubmit() {
        return onSubmit;
    }

    /**
     * Create a select menu which only works if the provided predicate is true, and do something if false
     */
    public static BaseSelectMenu predicate(CommandPredicate<SelectMenuEvent> predicate,
                                           CommandConsumer<SelectMenuEvent> ifFalse, SelectMenu selectMenu,
                                           CommandConsumer<SelectMenuEvent> onClick) {
        return new BaseSelectMenu(selectMenu, event -> {
            if (!predicate.test(event)) ifFalse.accept(event);
            else if (onClick != null) onClick.accept(event);
        });
    }

    /**
     * Create a select menu which only one user can use
     */
    public static BaseSelectMenu user(Long userId, SelectMenu selectMenu,
                                      CommandConsumer<SelectMenuEvent> onClick) {
        return group(new Long[]{ userId }, selectMenu, onClick);
    }

    /**
     * Create a select menu which only a specific group of users can use
     */
    public static BaseSelectMenu group(Long[] userIds, SelectMenu selectMenu,
                                       CommandConsumer<SelectMenuEvent> onClick) {
        return predicate(event -> List.of(userIds).contains(event.getUser().getIdLong()),
            event -> event.reply(new PresetBuilder(Colossus.getErrorPresetType())
                .setTitle("Not Allowed")
                .setDescription("You're not allowed to use this button.")
            ), selectMenu, onClick);
    }

    @Override
    public ActionRow toActionRow() {
        return ActionRow.of(getSelectMenu());
    }

    @Override
    public void startListening(Message message) {
        SelectMenuEvent.addListener(
            message.getIdLong(), this,
            () -> message.editMessageComponents(Collections.emptyList()).queue()
        );
    }
}
