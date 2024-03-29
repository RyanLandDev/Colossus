package net.ryanland.colossus.sys.interactions.menu.selectrow;

import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.events.repliable.RepliableEvent;
import net.ryanland.colossus.sys.interactions.ComponentRow;
import net.ryanland.colossus.sys.interactions.menu.InteractionMenu;
import net.ryanland.colossus.sys.interactions.select.BaseSelectMenu;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link InteractionMenu} where a select menu is displayed,
 * each option revealing a unique set of {@link ComponentRow ComponentRows}.
 */
public class SelectRowMenu implements InteractionMenu {

    private final List<SelectRowOption> options;
    private final PresetBuilder startMessage;

    public SelectRowMenu(List<SelectRowOption> options, PresetBuilder startMessage) {
        this.options = options;
        this.startMessage = startMessage;
    }

    @Override
    public void send(RepliableEvent event) throws CommandException {
        startMessage.getComponentRows().add(0, renderSelectMenu(options, null));
        event.reply(startMessage);
    }

    public static PresetBuilder renderMessage(List<SelectRowOption> options, SelectRowOption option) {
        PresetBuilder message = option.getMessage();
        message.setComponentRows(new ArrayList<>(option.getRows()));
        message.getComponentRows().add(0, renderSelectMenu(options, option));
        return message;
    }

    public static void sendMessage(List<SelectRowOption> options, RepliableEvent event, SelectRowOption option) {
        event.reply(renderMessage(options, option));
    }

    public static BaseSelectMenu renderSelectMenu(List<SelectRowOption> options, SelectRowOption selected) {
        StringSelectMenu.Builder builder = StringSelectMenu.create("selectrowmenu");
        for (SelectRowOption option : options) {
            builder.addOption(option.getName(), option.getName(), option.getDescription(), option.getEmoji());
        }
        if (selected != null) builder.setDefaultValues(selected.getName());
        return new BaseSelectMenu(builder.build(), event -> {
            sendMessage(options, event, options.stream().filter(option -> option.getName().equals(event.getValues().get(0))).toList().get(0));
        });
    }
}
