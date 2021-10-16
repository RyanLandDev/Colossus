package net.ryanland.colossus.command.inhibitors;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.cooldown.CooldownHandler;
import net.ryanland.colossus.events.ContentCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CooldownInhibitor extends Inhibitor {

    @Override
    public boolean check(ContentCommandEvent event) {
        return event.getCommand().hasCooldown() && CooldownHandler.isCooldownActive(event);
    }

    @Override
    public PresetBuilder buildMessage(ContentCommandEvent event) {
        return new PresetBuilder(
            Colossus.getErrorPresetType(),
            "This command is currently on cooldown.\nTime left: " +
                formatRelative(new Date(
                    CooldownHandler.getActiveCooldown(event).expires().getTime() - System.currentTimeMillis())),
            "On Cooldown"
        );
    }

    private static String formatRelative(Date date) {
        String formatted = new SimpleDateFormat("D'd' H'h' m'm' s's'")
            .format(new Date(date.getTime() + Colossus.TIMEZONE_OFFSET));

        // Get the first number (the day) and decrease it by 1
        Matcher matcher = Pattern.compile("^\\d+").matcher(formatted);
        while (matcher.find()) {
            formatted = formatted.replaceFirst("^\\d+",
                String.valueOf(Integer.parseInt(matcher.group()) - 1));
        }

        // This regex will remove all spaces and leading 0s, except the last leading 0
        return formatted.replaceAll("^((^| )0[^\\d ](?!$)+)*| ", "");
    }
}
