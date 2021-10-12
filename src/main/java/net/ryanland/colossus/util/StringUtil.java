package net.ryanland.colossus.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StringUtil {

    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    @Contract(pure = true)
    public static @NotNull String genTrimProofSpaces(int amount) {
        return (" \u200b").repeat(amount);
    }

}
