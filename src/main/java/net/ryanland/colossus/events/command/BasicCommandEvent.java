package net.ryanland.colossus.events.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.events.ModalSubmitEvent;
import net.ryanland.colossus.events.repliable.RepliableEvent;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.Supply;
import net.ryanland.colossus.sys.message.PresetBuilder;

public sealed abstract class BasicCommandEvent implements RepliableEvent permits CommandEvent, ContextCommandEvent {

    protected RepliableEvent repliableEvent;

    /**
     * Sets the {@link RepliableEvent} used for reply methods in this class.
     */
    public void setRepliableEvent(RepliableEvent repliableEvent) {
        this.repliableEvent = repliableEvent;
    }

    public abstract BasicCommand getCommand();

    public abstract Event getEvent();

    public abstract JDA getJDA();

    public abstract String getName();

    public abstract MessageChannel getChannel();

    public abstract boolean isFromGuild();

    public DiscordLocale getGuildLocale() {
        return getGuild().getLocale();
    }

    public abstract DiscordLocale getUserLocale();

    /**
     * Gets the localization of the locale given by {@link #getUserLocale()} with the provided key
     */
    public String getLocalization(String key) {
        return Colossus.getLocalization(getUserLocale(), key);
    }

    /**
     * Get the {@link Supply} of the user who executed this command.
     * <br>Note: This method may produce an error if the {@link DatabaseDriver} is not properly configured.
     * @see Supply
     * @see DatabaseDriver
     */
    public Supply getUserSupply() {
        return getUser().getSupply();
    }

    /**
     * Get the {@link Supply} of the guild this command was executed in.
     * This will be {@code null} if the command was executed in DMs.
     * <br>Note: This method may produce an error if the {@link DatabaseDriver} is not properly configured.
     * @see Supply
     * @see DatabaseDriver
     */
    public Supply getGuildSupply() {
        if (getGuild() == null) return null;
        return getGuild().getSupply();
    }

    @Override
    public final void reply(String message, boolean ephemeral) {
        repliableEvent.reply(message, ephemeral);
    }

    @Override
    public final void reply(MessageEmbed message, boolean ephemeral) {
        repliableEvent.reply(message, ephemeral);
    }

    @Override
    public final void reply(PresetBuilder message) {
        repliableEvent.reply(message);
    }

    @Override
    public final void reply(Modal modal, CommandConsumer<ModalSubmitEvent> action) {
        repliableEvent.reply(modal, action);
    }

}
