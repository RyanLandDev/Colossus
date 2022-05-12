package net.ryanland.colossus.sys.message;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.interactions.InteractionUtil;
import net.ryanland.colossus.sys.interactions.button.ButtonRow;
import net.ryanland.colossus.sys.interactions.button.BaseButton;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PresetBuilder {

    private String content;
    private String title;
    private String description;
    private OffsetDateTime timestamp;
    private Integer color;
    private String thumbnail;
    private String authorName;
    private String authorUrl;
    private String authorIconUrl;
    private String footerText;
    private String footerIconUrl;
    private String image;
    private List<MessageEmbed.Field> fields;
    private boolean ephemeral;
    private List<ButtonRow> buttonRows;

    public PresetBuilder() {
        this(Colossus.getDefaultPresetType());
    }

    public PresetBuilder(PresetType type) {
        this(type, null);
    }

    public PresetBuilder(PresetType type, String description) {
        this(type, null, description);
    }

    public PresetBuilder(String description) {
        this((String) null, description);
    }

    public PresetBuilder(String title, String description) {
        this(Colossus.getDefaultPresetType(), title, description);
    }

    public PresetBuilder(PresetType type, String title, String description) {
        this.content = type.getContent();
        this.title = title;
        this.description = description;
        this.timestamp = type.getTimestamp();
        this.color = type.getColor();
        this.thumbnail = type.getThumbnail();
        this.authorName = type.getAuthorName();
        this.authorUrl = type.getAuthorUrl();
        this.authorIconUrl = type.getAuthorIconUrl();
        this.footerText = type.getFooterText();
        this.footerIconUrl = type.getFooterIconUrl();
        this.image = type.getImage();
        this.fields = new ArrayList<>(Arrays.asList(type.getFields()));
        this.ephemeral = type.isEphemeral();
        this.buttonRows = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }

    public PresetBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PresetBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PresetBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public PresetBuilder setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Integer getColor() {
        return color;
    }

    public PresetBuilder setColor(Integer color) {
        this.color = color;
        return this;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public PresetBuilder setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getFooterText() {
        return footerText;
    }

    public PresetBuilder setFooterText(String footerText) {
        this.footerText = footerText;
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

    public List<MessageEmbed.Field> getFields() {
        return fields;
    }

    public PresetBuilder setFields(List<MessageEmbed.Field> fields) {
        this.fields = fields;
        return this;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    public PresetBuilder setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
        return this;
    }

    /**
     * Sets the embed thumbnail to the bot's profile picture.
     * @return The builder
     * @see #setThumbnail(String)
     */
    public PresetBuilder addLogo() {
        setThumbnail(Colossus.getSelfUser().getAvatarUrl());
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

    public List<ButtonRow> getButtonRows() {
        return buttonRows;
    }

    public PresetBuilder addButtonRow(ButtonRow buttonRow) {
        buttonRows.add(buttonRow);
        return this;
    }

    public PresetBuilder setButtonRows(List<ButtonRow> buttonRows) {
        this.buttonRows = buttonRows;
        return this;
    }

    /**
     * Creates one or more {@link ButtonRow}s and adds them to this {@link PresetBuilder} based on the buttons provided.
     * @see #addButtons(List)
     * @see #addButtonRow(ButtonRow)
     * @see InteractionUtil#ofBase(List)
     */
    public PresetBuilder addButtons(BaseButton... buttons) {
        return addButtons(List.of(buttons));
    }

    /**
     * Creates one or more {@link ButtonRow}s and adds them to this {@link PresetBuilder} based on the buttons provided.
     * @see #addButtons(BaseButton...)
     * @see #addButtonRow(ButtonRow)
     * @see InteractionUtil#ofBase(List)
     */
    public PresetBuilder addButtons(List<BaseButton> buttons) {
        buttonRows.addAll(InteractionUtil.ofBase(buttons));
        return this;
    }

    public PresetBuilder clearButtons() {
        setButtonRows(new ArrayList<>());
        return this;
    }

    /**
     * Returns a new {@link EmbedBuilder} with all values set in this {@link PresetBuilder}.
     * <br>Note: The content, ephemeral boolean value and attached {@link BaseButton}s will be ignored.
     * @see #embed()
     */
    public EmbedBuilder embedBuilder() {
        EmbedBuilder builder = new EmbedBuilder()
            .setTitle(title)
            .setDescription(description)
            .setTimestamp(timestamp)
            .setColor(color)
            .setThumbnail(thumbnail)
            .setAuthor(authorName, authorUrl, authorIconUrl)
            .setFooter(footerText, footerIconUrl)
            .setImage(image);
        for (MessageEmbed.Field field : fields) {
            builder.addField(field);
        }
        return builder;
    }

    /**
     * Builds this {@link PresetBuilder} into a {@link MessageEmbed}.
     * <br>Note: The content, ephemeral boolean value and attached {@link BaseButton}s will be ignored.
     * @see #embedBuilder()
     */
    public MessageEmbed embed() {
        return embedBuilder().build();
    }
}
