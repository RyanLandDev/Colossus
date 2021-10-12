package net.ryanland.colossus.sys.message.builders;

public enum PresetType {
    //DEFAULT(0x2f3136), //(Seamless color)
    DEFAULT(0xffa500),
    NOTIFICATION(0x5dadec),
    ERROR(0xdd2e44, "Error", true),
    WARNING(0xffcc4d, "Warning"),
    SUCCESS(0x4ccd6a, "Success");

    private final int color;
    private final String defaultTitle;
    private final boolean showFooter;
    private final boolean isEphemeral;

    PresetType(int color) {
        this(color, null);
    }

    PresetType(int color, String defaultTitle) {
        this(color, defaultTitle, false);
    }

    PresetType(int color, boolean showFooter) {
        this(color, null, showFooter, false);
    }

    PresetType(int color, String defaultTitle, boolean isEphemeral) {
        this(color, defaultTitle, true, isEphemeral);
    }

    PresetType(int color, String defaultTitle, boolean showFooter, boolean isEphemeral) {
        this.color = color;
        this.defaultTitle = defaultTitle;
        this.showFooter = showFooter;
        this.isEphemeral = isEphemeral;
    }

    public int getColor() {
        return color;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public boolean shouldShowFooter() {
        return showFooter;
    }

    public boolean isEphemeral() {
        return isEphemeral;
    }
}
