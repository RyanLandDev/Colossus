package dev.ryanland.colossus.sys.presetbuilder;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

/**
 * Type of preset to use when building embeds with {@link PresetBuilder}.
 * @see PresetBuilder
 * @see DefaultPresetType
 */
public interface PresetType {

    default @Nullable String getContent() {
        return null;
    }

    default @Nullable String getTitle() {
        return null;
    }

    default @Nullable String getDescription() {
        return null;
    }

    default @Nullable OffsetDateTime getTimestamp() {
        return null;
    }

    default @Nullable Integer getColor() {
        return null;
    }

    default @Nullable String getThumbnail() {
        return null;
    }

    default @Nullable String getAuthorName() {
        return null;
    }

    default @Nullable String getAuthorUrl() {
        return null;
    }

    default @Nullable String getAuthorIconUrl() {
        return null;
    }

    default @Nullable String getFooterText() {
        return null;
    }

    default @Nullable String getFooterIconUrl() {
        return null;
    }

    default @Nullable String getImage() {
        return null;
    }

    default @Nullable MessageEmbed.Field[] getFields() {
        return new MessageEmbed.Field[]{};
    }

    default boolean isEphemeral() {
        return false;
    }

    /**
     * Create a new {@link PresetBuilder} with this preset type.
     */
    default PresetBuilder create() {
        return new PresetBuilder(this);
    }
}
