package net.ryanland.colossus.sys.message.builders;

import net.ryanland.colossus.util.NumberUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

import static net.ryanland.colossus.util.NumberUtil.format;

public record InfoValue(Type type, @NotNull String emoji, String currentValue, @Nullable String nextValue,
                        String title) implements Emojis {

    public InfoValue(Type type, @NotNull String emoji, Number currentValue, @Nullable Number nextValue, String title) {
        this(type, emoji, format(currentValue), format(nextValue), title);
    }

    public InfoValue(Type type, @NotNull String emoji, String currentValue, @Nullable String nextValue) {
        this(type, emoji, currentValue, nextValue, "");
    }

    public InfoValue(Type type, @NotNull String emoji, Number currentValue, @Nullable Number nextValue) {
        this(type, emoji, format(currentValue), format(nextValue));
    }

    public String buildRegular() {
        return String.format("%s%s%s",
            title.isEmpty() ? "" : "**" + title + ":** ",
            emoji.isEmpty() ? "" : emoji + " ",
            currentValue
        );
    }

    public String buildUpgradable() {
        return String.format("%s%s%s",
            title.isEmpty() ? "" : "**" + title + ":** ",
            emoji.isEmpty() ? "" : emoji + " ",
            Objects.equals(currentValue, nextValue) ? currentValue :
                String.format("%s %s *%s*", currentValue, ARROW_RIGHT, nextValue)
        );
    }

    public String buildCapacitable(boolean includeFull) {
        return String.format("%s%s%s / %s%s",
            title.isEmpty() ? "" : "**" + title + ":** ",
            emoji.isEmpty() ? "" : emoji + " ",
            currentValue, nextValue,
            includeFull && NumberUtil.of(currentValue) >= NumberUtil.of(nextValue) ? " **FULL**" : ""
        );
    }

    public enum Type {

        REGULAR(InfoValue::buildRegular),
        UPGRADABLE(InfoValue::buildUpgradable),
        CAPACITABLE(v -> v.buildCapacitable(false));

        private final Function<InfoValue, String> builder;

        Type(Function<InfoValue, String> builder) {
            this.builder = builder;
        }

        public Function<InfoValue, String> getBuilder() {
            return builder;
        }
    }
}
