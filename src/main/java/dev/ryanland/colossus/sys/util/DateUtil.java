package dev.ryanland.colossus.sys.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    private static final int TIME_OFFSET = -3600000;

    public static String formatRelative(Date date) {
        date = new Date(date.getTime() + TIME_OFFSET);

        String formatted = new SimpleDateFormat("D'd' H'h' m'm' s's'")
            .format(date);

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
