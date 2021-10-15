package net.ryanland.colossus.sys.interactions;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.ryanland.colossus.bot.command.CommandException;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.message.DefaultPresetType;
import net.ryanland.colossus.sys.ExecutorUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ButtonHandler {

    private static final HashMap<Long, Function<ButtonClickEvent, ButtonListener>> BUTTON_LISTENERS = new HashMap<>();

    public static void addListener(InteractionHook hook,
                                   Function<ButtonClickEvent, ButtonListener> buttonListener) {
        Long hookId = hook.retrieveOriginal().complete().getIdLong();

        BUTTON_LISTENERS.put(hookId, buttonListener);

        ExecutorUtil.schedule(
            () -> hook.editOriginal("").setActionRows(Collections.emptyList()).queue(),
            2, TimeUnit.MINUTES);
    }

    public static void handleEvent(ButtonClickEvent event) throws CommandException {
        if (!BUTTON_LISTENERS.containsKey(event.getMessageIdLong())) {
            return;
        }

        ButtonListener listener = BUTTON_LISTENERS.get(event.getMessageIdLong()).apply(event);

        if (listener != null) {
            if (event.getUser().getIdLong() != listener.userId()) {

                event.deferReply(true).addEmbeds(
                    new PresetBuilder(DefaultPresetType.ERROR, "You can't use this button.").build()
                ).queue();

                return;
            }

            ButtonClickContainer clickContainer = listener.container().apply(event);
            clickContainer.onClick()
                .consume(event, clickContainer.value().apply(event));
        }
    }

    public record ButtonListener(long userId, Function<ButtonClickEvent, ButtonClickContainer> container) {

    }

}
