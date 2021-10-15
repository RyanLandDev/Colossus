package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.interactions.ButtonClickContainer;
import net.ryanland.colossus.sys.interactions.ButtonHandler;
import net.ryanland.colossus.sys.interactions.InteractionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TabMenu implements InteractionMenu {

    private final TabMenuPage[] pages;

    public TabMenu(List<TabMenuPage> pages) {
        this(pages.toArray(new TabMenuPage[0]));
    }

    public TabMenu(TabMenuPage[] pages) {
        this.pages = pages;
    }

    @Override
    public void send(Interaction interaction) {
        // Set all embed titles to match the category name
        for (TabMenuPage page : pages) {
            PresetBuilder embed = page.getEmbed();

            if (embed.getTitle() == null) {
                embed.setTitle(page.getName());
            }
        }

        // Init vars
        HashMap<String, TabMenuPage> pageMap = new HashMap<>(pages.length);
        List<Button> buttons = new ArrayList<>();

        // Iterate over all pages
        for (TabMenuPage page : pages) {
            // Skip if this page should be hidden
            if (page.isHidden()) {
                continue;
            }

            // Create the Button
            Button button = Button.secondary(page.getName(), page.getName());
            if (page.getEmoji() != null) {
                button = button.withEmoji(Emoji.fromMarkdown(page.getEmoji()));
            }

            // Put the page and buttons in their respective data structures
            pageMap.put(button.getId(), page);
            buttons.add(button);
        }

        // Send the message and set the action rows
        InteractionHook hook = interaction.replyEmbeds(pages[0].getEmbed().build())
            .addActionRows(InteractionUtil.of(buttons))
            .complete();

        // Add a listener for when a button is clicked
        ButtonHandler.addListener(hook,
            buttonEvent -> new ButtonHandler.ButtonListener(
                interaction.getUser().getIdLong(),
                clickEvent -> new ButtonClickContainer(
                    event -> {
                        event.deferEdit().queue();

                        hook.editOriginalEmbeds(pageMap.get(event.getComponentId())
                                .getEmbed().build())
                            .setActionRows(
                                InteractionUtil.of(buttons.stream()
                                    .map(button -> button.equals(event.getButton()) ?
                                        button.withStyle(ButtonStyle.SUCCESS).asDisabled() :
                                        button.withStyle(ButtonStyle.SECONDARY).asEnabled())
                                    .collect(Collectors.toList())
                                ))
                            .queue();
                    })));
    }

}
