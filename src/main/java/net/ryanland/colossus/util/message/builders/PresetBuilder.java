package net.ryanland.colossus.util.message.builders;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.ryanland.colossus.Colossus;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PresetBuilder {

    private int color;
    private String description;
    private String title;
    private String authorName;
    private String authorUrl;
    private String authorIconUrl;
    private String footer;
    private String footerIconUrl;
    private String image;
    private String thumbnail;
    private TemporalAccessor timestamp;
    private List<MessageEmbed.Field> fields = new LinkedList<>();
    private boolean isEphemeral;

    public PresetBuilder() {
        this((String) null);
    }

    public PresetBuilder(String description) {
        this(description, null);
    }

    public PresetBuilder(String description, String title) {
        this(PresetType.DEFAULT, description, title);
    }

    public PresetBuilder(PresetType type) {
        this(type, null);
    }

    public PresetBuilder(PresetType type, String description) {
        this(type, description, type.getDefaultTitle());
    }

    public PresetBuilder(PresetType type, String description, String title) {
        this(type, description, title, type.shouldShowFooter());
    }

    public PresetBuilder(PresetType type, String description, String title, boolean showFooter) {
        color = type.getColor();
        this.description = description;
        this.title = title;
        if (showFooter) {
            footer = Colossus.getSelfUser().getName();
            footerIconUrl = Colossus.getSelfUser().getAvatarUrl();
            timestamp = Instant.now();
        }
        isEphemeral = type.isEphemeral();
    }

    public boolean isEphemeral() {
        return isEphemeral;
    }

    public PresetBuilder setEphemeral(boolean isEphemeral) {
        this.isEphemeral = isEphemeral;
        return this;
    }

    public int getColor() {
        return color;
    }

    public PresetBuilder setColor(int color) {
        this.color = color;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PresetBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PresetBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public PresetBuilder setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public PresetBuilder setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    public String getAuthorIconUrl() {
        return authorIconUrl;
    }

    public PresetBuilder setAuthorIconUrl(String authorIconUrl) {
        this.authorIconUrl = authorIconUrl;
        return this;
    }

    public String getFooter() {
        return footer;
    }

    public PresetBuilder setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public String getFooterIconUrl() {
        return footerIconUrl;
    }

    public PresetBuilder setFooterIconUrl(String footerIconUrl) {
        this.footerIconUrl = footerIconUrl;
        return this;
    }

    public String getImage() {
        return image;
    }

    public PresetBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public PresetBuilder setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    /**
     * Sets the embed thumbnail to the bot's profile picture.
     *
     * @return {@code this}
     */
    public PresetBuilder addLogo() {
        thumbnail = Colossus.getSelfUser().getAvatarUrl();
        return this;
    }

    public TemporalAccessor getTimestamp() {
        return timestamp;
    }

    public PresetBuilder setTimestamp(TemporalAccessor timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public List<MessageEmbed.Field> getFields() {
        return fields;
    }

    public PresetBuilder setFields(List<MessageEmbed.Field> fields) {
        this.fields = fields;
        return this;
    }

    public PresetBuilder addField(String name, String value) {
        return addField(name, value, false);
    }

    public PresetBuilder addField(String name, String value, boolean inline) {
        return addField(new MessageEmbed.Field(name, value, inline));
    }

    public PresetBuilder addField(MessageEmbed.Field field) {
        fields.add(field);
        return this;
    }

    public PresetBuilder addFields(MessageEmbed.Field... fields) {
        this.fields.addAll(Arrays.asList(fields));
        return this;
    }

    public EmbedBuilder builder() {
        EmbedBuilder builder = new EmbedBuilder()
            .setTitle(title)
            .setAuthor(authorName, authorUrl, authorIconUrl)
            .setDescription(description)
            .setColor(color)
            .setThumbnail(thumbnail)
            .setImage(image)
            .setFooter(footer, footerIconUrl)
            .setTimestamp(timestamp);

        for (MessageEmbed.Field field : fields) {
            builder.addField(field);
        }

        return builder;
    }

    public MessageEmbed build() {
        return builder().build();
    }
}
