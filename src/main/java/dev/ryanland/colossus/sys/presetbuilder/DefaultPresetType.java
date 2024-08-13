package dev.ryanland.colossus.sys.presetbuilder;

import dev.ryanland.colossus.Colossus;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

/**
 * Example implementation of {@link PresetType}, intended for quick setup as alternative to setting up your own enum
 */
public enum DefaultPresetType implements PresetType {

    EMPTY(() -> null, () -> null, () -> null, () -> null, () -> Role.DEFAULT_COLOR_RAW,
        () -> null, () -> null, () -> null, () -> null,
        () -> null, () -> null,
        () -> null, () -> null, () -> false),

    DEFAULT(() -> null, () -> null, () -> null, OffsetDateTime::now, () -> 0x2f3136,
        () -> null, () -> null, () -> null, () -> null,
        () -> Colossus.getSelfUser().getName(), () -> Colossus.getSelfUser().getAvatarUrl(),
        () -> null, () -> null, () -> false),

    NOTIFICATION(() -> null, () -> null, () -> null, OffsetDateTime::now, () -> 0x5dadec,
        () -> Colossus.getSelfUser().getAvatarUrl(), () -> null, () -> null, () -> null,
        () -> Colossus.getSelfUser().getName(), () -> Colossus.getSelfUser().getAvatarUrl(),
        () -> null, () -> null, () -> false),

    ERROR(() -> null, () -> "Error", () -> null, OffsetDateTime::now, () -> 0xdd2e44,
        () -> null, () -> null, () -> null, () -> null,
        () -> Colossus.getSelfUser().getName(), () -> Colossus.getSelfUser().getAvatarUrl(),
        () -> null, () -> null, () -> true),

    WARNING(() -> null, () -> "Warning", () -> null, OffsetDateTime::now, () -> 0xffcc4d,
        () -> null, () -> null, () -> null, () -> null,
        () -> Colossus.getSelfUser().getName(), () -> Colossus.getSelfUser().getAvatarUrl(),
        () -> null, () -> null, () -> false),

    SUCCESS(() -> null, () -> "Success", () -> null, OffsetDateTime::now, () -> 0x4ccd6a,
        () -> null, () -> null, () -> null, () -> null,
        () -> Colossus.getSelfUser().getName(), () -> Colossus.getSelfUser().getAvatarUrl(),
        () -> null, () -> null, () -> false)

    ;

    private final Supplier<String> content;
    private final Supplier<String> title;
    private final Supplier<String> description;
    private final Supplier<OffsetDateTime> timestamp;
    private final Supplier<Integer> color;
    private final Supplier<String> thumbnail;
    private final Supplier<String> authorName;
    private final Supplier<String> authorUrl;
    private final Supplier<String> authorIconUrl;
    private final Supplier<String> footerText;
    private final Supplier<String> footerIconUrl;
    private final Supplier<String> image;
    private final Supplier<MessageEmbed.Field[]> fields;
    private final Supplier<Boolean> ephemeral;

    DefaultPresetType(Supplier<String> content, Supplier<String> title, Supplier<String> description,
                      Supplier<OffsetDateTime> timestamp, Supplier<Integer> color, Supplier<String> thumbnail,
                      Supplier<String> authorName, Supplier<String> authorUrl, Supplier<String> authorIconUrl,
                      Supplier<String> footerText, Supplier<String> footerIconUrl, Supplier<String> image,
                      Supplier<MessageEmbed.Field[]> fields, Supplier<Boolean> ephemeral) {
        this.content = content;
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
    public String getContent() {
        return content.get();
    }

    @Nullable
    @Override
    public String getTitle() {
        return title.get();
    }

    @Nullable
    @Override
    public String getDescription() {
        return description.get();
    }

    @Nullable
    @Override
    public OffsetDateTime getTimestamp() {
        return timestamp.get();
    }

    @Nullable
    @Override
    public Integer getColor() {
        return color.get();
    }

    @Nullable
    @Override
    public String getThumbnail() {
        return thumbnail.get();
    }

    @Nullable
    @Override
    public String getAuthorName() {
        return authorName.get();
    }

    @Nullable
    @Override
    public String getAuthorUrl() {
        return authorUrl.get();
    }

    @Nullable
    @Override
    public String getAuthorIconUrl() {
        return authorIconUrl.get();
    }

    @Nullable
    @Override
    public String getFooterText() {
        return footerText.get();
    }

    @Nullable
    @Override
    public String getFooterIconUrl() {
        return footerIconUrl.get();
    }

    @Nullable
    @Override
    public String getImage() {
        return image.get();
    }

    @Nullable
    @Override
    public MessageEmbed.Field[] getFields() {
        if (fields.get() == null) return new MessageEmbed.Field[]{};
        else return fields.get();
    }

    @Override
    public boolean isEphemeral() {
        return ephemeral.get();
    }
}
