package net.ryanland.colossus.sys.message;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.ryanland.colossus.Colossus;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

/**
 * Example implementation of {@link PresetType}, intended for quick setup as alternative to setting up your own enum
 */
public enum DefaultPresetType implements PresetType {

    DEFAULT(null, null, OffsetDateTime.now(), 0x2f3136,
        null, null, null, null,
        Colossus.getSelfUser().getName(), Colossus.getSelfUser().getAvatarUrl(),
        null, null, false),

    NOTIFICATION(null, null, OffsetDateTime.now(), 0x5dadec,
        Colossus.getSelfUser().getAvatarUrl(), null, null, null,
        Colossus.getSelfUser().getName(), Colossus.getSelfUser().getAvatarUrl(),
        null, null, false),

    ERROR("Error", null, OffsetDateTime.now(), 0xdd2e44,
        null, null, null, null,
        Colossus.getSelfUser().getName(), Colossus.getSelfUser().getAvatarUrl(),
        null, null, true),

    WARNING("Warning", null, OffsetDateTime.now(), 0xffcc4d,
        null, null, null, null,
        Colossus.getSelfUser().getName(), Colossus.getSelfUser().getAvatarUrl(),
        null, null, false),

    SUCCESS("Success", null, OffsetDateTime.now(), 0x4ccd6a,
        null, null, null, null,
        Colossus.getSelfUser().getName(), Colossus.getSelfUser().getAvatarUrl(),
        null, null, false)

    ;

    private final String title;
    private final String description;
    private final OffsetDateTime timestamp;
    private final Integer color;
    private final String thumbnail;
    private final String authorName;
    private final String authorUrl;
    private final String authorIconUrl;
    private final String footerText;
    private final String footerIconUrl;
    private final String image;
    private final MessageEmbed.Field[] fields;
    private final boolean ephemeral;

    DefaultPresetType(String title, String description, OffsetDateTime timestamp, Integer color,
                      String thumbnail, String authorName, String authorUrl, String authorIconUrl,
                      String footerText, String footerIconUrl, String image, MessageEmbed.Field[] fields,
                      boolean ephemeral) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.color = color;
        this.thumbnail = thumbnail;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.authorIconUrl = authorIconUrl;
        this.footerText = footerText;
        this.footerIconUrl = footerIconUrl;
        this.image = image;
        this.fields = fields;
        this.ephemeral = ephemeral;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getDescription() {
        return description;
    }

    @Nullable
    @Override
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    @Nullable
    @Override
    public Integer getColor() {
        return color;
    }

    @Nullable
    @Override
    public String getThumbnail() {
        return thumbnail;
    }

    @Nullable
    @Override
    public String getAuthorName() {
        return authorName;
    }

    @Nullable
    @Override
    public String getAuthorUrl() {
        return authorUrl;
    }

    @Nullable
    @Override
    public String getAuthorIconUrl() {
        return authorIconUrl;
    }

    @Nullable
    @Override
    public String getFooterText() {
        return footerText;
    }

    @Nullable
    @Override
    public String getFooterIconUrl() {
        return footerIconUrl;
    }

    @Nullable
    @Override
    public String getImage() {
        return image;
    }

    @Nullable
    @Override
    public MessageEmbed.Field[] getFields() {
        return fields;
    }

    @Override
    public boolean isEphemeral() {
        return ephemeral;
    }
}
