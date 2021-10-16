package net.ryanland.colossus.command.info;

public enum Category {

    INFORMATION("Information", "Commands to get general information.", "📋"),
    DEVELOPER("Developer", "Utility commands for bot developers only.", "💻"),

    EVENT_MANAGEMENT("Event Management", "Host and start community events.", "🎯");

    private final String name;
    private final String description;
    private final String emoji;

    Category(String name, String description, String emoji) {
        this.name = name;
        this.description = description;
        this.emoji = emoji;
    }

    public static Category[] getCategories() {
        return values();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
    }
}
