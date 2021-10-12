package net.ryanland.colossus.util;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtil {

    /**
     * Equivalent of {@link NumberFormat#format(long)} with {@link Locale}{@code .US}.
     *
     * @param number Number to format.
     * @return The formatted number string.
     */
    public static String format(Number number) {
        if (number == null) {
            return null;
        }

        return NumberFormat.getInstance(Locale.US).format(number);
    }

    public static Integer of(String formattedNumber) {
        return Integer.parseInt(formattedNumber.replaceAll(",", ""));
    }

    public static int clamp(int value, int max) {
        return clamp(value, 0, max);
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static boolean inRange(int number, int min, int max) {
        return number >= min && number <= max;
    }

    public static boolean withinRange(int number, int min, int max) {
        return number > min && number < max;
    }

    /**
     * Rounds to the nearest multiple provided.
     */
    public static double round(double multiple, double value) {
        return multiple * (Math.round(value / multiple));
    }

}
