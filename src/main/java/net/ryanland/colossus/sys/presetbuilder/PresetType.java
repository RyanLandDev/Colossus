package net.ryanland.colossus.sys.presetbuilder;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

/**
 * Type of preset to use when building embeds with {@link PresetBuilder}.
 * @see PresetBuilder
 * @see DefaultPresetType
 */
public interface PresetType {

    @Nullable String getContent();

    @Nullable String getTitle();

    @Nullable String getDescription();

    @Nullable OffsetDateTime getTimestamp();

    @Nullable Integer getColor();

    @Nullable String getThumbnail();

    @Nullable String getAuthorName();

    @Nullable String getAuthorUrl();

    @Nullable String getAuthorIconUrl();

    @Nullable String getFooterText();

    @Nullable String getFooterIconUrl();

    @Nullable String getImage();

    @Nullable MessageEmbed.Field[] getFields();

    boolean isEphemeral();

}
