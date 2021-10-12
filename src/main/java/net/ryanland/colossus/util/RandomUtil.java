package net.ryanland.colossus.util;

import java.util.Random;

public class RandomUtil {

    public static int randomInt(int min, int max) {
        return new Random().nextInt((max + 1) - min) + min;
    }

    public static float randomFloat(float min, float max) {
        min = min * 100000;
        max = max * 100000;
        float result = new Random().nextInt((int) (max - min)) + min;
        return result / 100000;
    }

    @SafeVarargs
    public static <T> T pickRandom(T... options) {
        return options[randomInt(0, options.length)];
    }
}
