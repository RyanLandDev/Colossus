package net.ryanland.colossus.sys.message.builders;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class InfoValueCollection extends ArrayList<InfoValue> {

    public InfoValueCollection addEmpty() {
        return addRegular("");
    }

    // Regular ------------------------------------------

    public InfoValueCollection addRegular(String title, String emoji, String value) {
        add(new InfoValue(InfoValue.Type.REGULAR, emoji, value, null, title));
        return this;
    }

    public InfoValueCollection addRegular(String title, String value) {
        return addRegular(title, "", value);
    }

    public InfoValueCollection addRegular(String value) {
        return addRegular("", value);
    }

    public InfoValueCollection addRegular(String title, String emoji, Number value) {
        add(new InfoValue(InfoValue.Type.REGULAR, emoji, value, null, title));
        return this;
    }

    public InfoValueCollection addRegular(String title, Number value) {
        return addRegular(title, "", value);
    }

    public InfoValueCollection addRegular(Number value) {
        return addRegular("", value);
    }

    // Upgradable -------------------------------------------

    public InfoValueCollection addUpgradable(String title, String emoji, String currentValue, String nextValue) {
        add(new InfoValue(InfoValue.Type.UPGRADABLE, emoji, currentValue, nextValue, title));
        return this;
    }

    public InfoValueCollection addUpgradable(String title, String currentValue, String nextValue) {
        return addCapacitable(title, "", currentValue, nextValue);
    }

    public InfoValueCollection addUpgradable(String currentValue, String nextValue) {
        return addCapacitable("", currentValue, nextValue);
    }

    public InfoValueCollection addUpgradable(String title, String emoji, Number currentValue, Number nextValue) {
        add(new InfoValue(InfoValue.Type.UPGRADABLE, emoji, currentValue, nextValue, title));
        return this;
    }

    public InfoValueCollection addUpgradable(String title, Number currentValue, Number nextValue) {
        return addCapacitable(title, "", currentValue, nextValue);
    }

    public InfoValueCollection addUpgradable(Number currentValue, Number nextValue) {
        return addCapacitable("", currentValue, nextValue);
    }

    // Capacitable -------------------------------------

    public InfoValueCollection addCapacitable(String title, String emoji, String currentValue, String nextValue) {
        add(new InfoValue(InfoValue.Type.CAPACITABLE, emoji, currentValue, nextValue, title));
        return this;
    }

    public InfoValueCollection addCapacitable(String title, String currentValue, String nextValue) {
        return addCapacitable(title, "", currentValue, nextValue);
    }

    public InfoValueCollection addCapacitable(String currentValue, String nextValue) {
        return addCapacitable("", currentValue, nextValue);
    }

    public InfoValueCollection addCapacitable(String title, String emoji, Number currentValue, Number nextValue) {
        add(new InfoValue(InfoValue.Type.CAPACITABLE, emoji, currentValue, nextValue, title));
        return this;
    }

    public InfoValueCollection addCapacitable(String title, Number currentValue, Number nextValue) {
        return addCapacitable(title, "", currentValue, nextValue);
    }

    public InfoValueCollection addCapacitable(Number currentValue, Number nextValue) {
        return addCapacitable("", currentValue, nextValue);
    }

    // Build -------------------------------------------------------------

    public String build() {
        return stream()
            .map(value -> value.type().getBuilder().apply(value))
            .collect(Collectors.joining("\n"));
    }
}
