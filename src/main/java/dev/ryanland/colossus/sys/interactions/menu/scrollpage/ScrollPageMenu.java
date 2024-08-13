package dev.ryanland.colossus.sys.interactions.menu.scrollpage;

import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.sys.interactions.ComponentRow;
import dev.ryanland.colossus.sys.interactions.button.BaseButton;
import dev.ryanland.colossus.sys.interactions.button.ButtonRow;
import dev.ryanland.colossus.sys.interactions.menu.InteractionMenu;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.events.repliable.RepliableEvent;

import java.util.ArrayList;
import java.util.List;

public class ScrollPageMenu implements InteractionMenu {

    private final List<ScrollPage> pages;
    private final int startPage;

    public ScrollPageMenu(List<PresetBuilder> pages, int startPage) {
        this.pages = ScrollPage.of(pages);
        this.startPage = startPage;
    }

    public ScrollPageMenu(List<PresetBuilder> pages) {
        this(pages, 0);
    }

    @Override
    public void send(RepliableEvent event) throws CommandException {
        event.reply(renderPage(event.getUser().getIdLong(), pages, startPage));
    }

    public static PresetBuilder renderPage(long userId, List<ScrollPage> pages, int page) {
        PresetBuilder message = pages.get(page).getMessage();
        message.setComponentRows(new ArrayList<>(pages.get(page).componentRows));
        message.getComponentRows().add(0, new ButtonRow(
            // previous page
            BaseButton.user(userId, Button.primary("previous", Emoji.fromUnicode("⬅")).withDisabled(page <= 0), event -> {
                event.reply(renderPage(userId, pages, page - 1));
            }),
            // current page
            BaseButton.user(userId, Button.secondary("current", "Page " + (page+1) + "/" + pages.size()).withDisabled(pages.size() <= 1), event -> {
                event.reply(Modal.create("page", "Select Page").addActionRow(TextInput
                    .create("page", "Page", TextInputStyle.SHORT).setPlaceholder("Enter page number...").build()).build(), evt -> {
                    int newPage;
                    try {
                        newPage = Integer.parseInt(evt.getValue("page").getAsString());
                    } catch (NumberFormatException e) {
                        newPage = 0;
                    }
                    if (newPage < 1 || newPage > pages.size()) {
                        evt.reply(new PresetBuilder(Colossus.getErrorPresetType(), "Invalid Page", "Invalid page number provided."));
                        return;
                    }
                    evt.reply(renderPage(userId, pages, newPage - 1));
                });
            }),
            // next page
            BaseButton.user(userId, Button.primary("next", Emoji.fromUnicode("➡")).withDisabled(page + 1 >= pages.size()), event -> {
                event.reply(renderPage(userId, pages, page + 1));
            })));
        return message;
    }

    public static final class ScrollPage {

        private final PresetBuilder message;
        private final List<ComponentRow> componentRows;

        public ScrollPage(PresetBuilder message) {
            this.message = message;
            this.componentRows = message.getComponentRows();
        }

        public PresetBuilder getMessage() {
            return message;
        }

        public List<ComponentRow> getComponentRows() {
            return componentRows;
        }

        public static List<ScrollPage> of(List<PresetBuilder> pages) {
            return pages.stream().map(ScrollPage::new).toList();
        }
    }
}
