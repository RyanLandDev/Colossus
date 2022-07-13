package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.ryanland.colossus.events.RepliableEvent;
import net.ryanland.colossus.sys.interactions.button.BaseButton;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayList;
import java.util.List;

public class TabMenu implements InteractionMenu {

    private final List<TabMenuPage> pages;
    private final TabMenuPage homePage;

    public TabMenu(List<TabMenuPage> pages, PresetBuilder homePage) {
        this.pages = pages;
        this.homePage = new TabMenuPage("_home", homePage, null, true);
    }

    @Override
    public void send(RepliableEvent event) {
        event.reply(renderPage(homePage, event.getUser().getIdLong()));
    }

    public PresetBuilder renderPage(TabMenuPage page, long userId) {
        boolean hasSubPages = !page.getChildren().isEmpty();
        List<BaseButton> buttons = new ArrayList<>();

        // if parent exists, add back button
        if (page.getParent() != null) {
            buttons.add(BaseButton.user(userId,
                Button.primary("_back", "Back").withEmoji(Emoji.fromUnicode("⬅")),
                evt -> evt.reply(renderPage(page.getParent(), userId))
            ));
        // if there is no parent, but we are currently in a subpage menu, add a back button to return to the home page
        } else if (hasSubPages) {
            buttons.add(BaseButton.user(userId,
                Button.primary("_back", "Back").withEmoji(Emoji.fromUnicode("⬅")),
                evt -> evt.reply(renderPage(homePage, userId))
            ));
        }

        // has subpages, display subpage buttons
        if (hasSubPages) {
            for (TabMenuPage subpage : page) {
                // skip hidden or invalid subpages
                if (subpage.getEmbed() == null || subpage.isHidden()) continue;

                // create the button
                Button button = Button.secondary("." + subpage.getName(), subpage.getName());
                if (subpage.getEmoji() != null) button = button.withEmoji(Emoji.fromFormatted(subpage.getEmoji()));
                buttons.add(BaseButton.user(userId, button, evt -> evt.reply(renderPage(subpage, userId))));
            }
        // has no subpages, display other page buttons
        } else {
            // if there is no parent page, show all top page buttons. else, show the subpage buttons of the parent page
            List<TabMenuPage> pages;
            if (page.getParent() == null) pages = this.pages;
            else pages = page.getParent().getChildren();

            // loop through pages
            for (TabMenuPage _page : pages) {
                if (_page.getEmbed() == null || _page.isHidden()) continue;
                Button button = Button.secondary("." + _page.getName(), _page.getName());
                if (_page.getEmoji() != null) button = button.withEmoji(Emoji.fromUnicode(_page.getEmoji()));
                buttons.add(BaseButton.user(userId,
                    _page.getName().equals(page.getName()) ? button.withStyle(ButtonStyle.SUCCESS).asDisabled() : button,
                    evt -> evt.reply(renderPage(_page, userId))
                ));
            }
        }

        return page.getEmbed().clearComponentRows().addButtons(buttons);
    }
}
