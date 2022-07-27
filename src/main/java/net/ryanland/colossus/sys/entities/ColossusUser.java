package net.ryanland.colossus.sys.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public record ColossusUser(User user) implements User, ColossusDatabaseEntity<User> {

    @Override
    public User getClient() {
        return user();
    }


    /**
     * The username of the {@link User User}. Length is between 2 and 32 characters (inclusive).
     *
     * @return Never-null String containing the {@link User User}'s username.
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @NotNull
    @Override
    public String getName() {
        return user().getName();
    }

    /**
     * <br>The discriminator of the {@link User User}. Used to differentiate between users with the same usernames.
     * <br>This only contains the 4 digits after the username and the #.
     *
     * @return Never-null String containing the {@link User User} discriminator.
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     *                                       Ex: 6297
     */
    @NotNull
    @Override
    public String getDiscriminator() {
        return user().getDiscriminator();
    }

    /**
     * The Discord ID for this user's avatar image.
     * If the user has not set an image, this will return null.
     *
     * @return Possibly-null String containing the {@link User User} avatar id.
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @Nullable
    @Override
    public String getAvatarId() {
        return user().getAvatarId();
    }

    /**
     * The Discord ID for this user's default avatar image.
     *
     * @return Never-null String containing the {@link User User} default avatar id.
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @NotNull
    @Override
    public String getDefaultAvatarId() {
        return user().getDefaultAvatarId();
    }

    /**
     * Loads the user's {@link User.Profile} data.
     * Returns a completed RestAction if this User has been retrieved using {@link JDA#retrieveUserById(long)}.
     * You can use {@link CacheRestAction#useCache(boolean) useCache(false)} to force the request for a new profile with up-to-date information.
     *
     * @throws UnsupportedOperationException
     *         If this User was created with {@link #fromId(long)}
     *
     * @return {@link CacheRestAction} - Type: {@link User.Profile}
     */
    @NotNull
    @Override
    public CacheRestAction<Profile> retrieveProfile() {
        return user().retrieveProfile();
    }

    /**
     * The "tag" for this user
     * <p>This is the equivalent of calling {@link String#format(String, Object...) String.format}("%#s", user)
     *
     * @return Never-null String containing the tag for this user, for example DV8FromTheWorld#6297
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @NotNull
    @Override
    public String getAsTag() {
        return user().getAsTag();
    }

    /**
     * Whether or not the currently logged in user and this user have a currently open
     * {@link PrivateChannel PrivateChannel} or not.
     *
     * @return True if the logged in account shares a PrivateChannel with this user.
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @Override
    public boolean hasPrivateChannel() {
        return user().hasPrivateChannel();
    }

    /**
     * Opens a {@link PrivateChannel} with this User.
     * <br>If a channel has already been opened with this user, it is immediately returned in the RestAction's
     * success consumer without contacting the Discord API.
     * You can use {@link CacheRestAction#useCache(boolean) useCache(false)} to force the request for a new channel object,
     * which is rarely useful since the channel id never changes.
     *
     * <p><b>Examples</b><br>
     * <pre>{@code
     * // Send message without response handling
     * public void sendMessage(User user, String content) {
     *     user.openPrivateChannel()
     *         .flatMap(channel -> channel.sendMessage(content))
     *         .queue();
     * }
     *
     * // Send message and delete 30 seconds later
     * public RestAction<Void> sendSecretMessage(User user, String content) {
     *     return user.openPrivateChannel() // RestAction<PrivateChannel>
     *                .flatMap(channel -> channel.sendMessage(content)) // RestAction<Message>
     *                .delay(30, TimeUnit.SECONDS) // RestAction<Message> with delayed response
     *                .flatMap(Message::delete); // RestAction<Void> (executed 30 seconds after sending)
     * }
     * }</pre>
     *
     * @throws UnsupportedOperationException
     *         If the recipient User is the currently logged in account (represented by {@link net.dv8tion.jda.api.entities.SelfUser SelfUser})
     *         or if the user was created with {@link #fromId(long)}
     *
     * @return {@link CacheRestAction} - Type: {@link PrivateChannel}
     *         <br>Retrieves the PrivateChannel to use to directly message this User.
     *
     * @see    JDA#openPrivateChannelById(long)
     */
    @NotNull
    @Override
    public CacheRestAction<PrivateChannel> openPrivateChannel() {
        return user().openPrivateChannel();
    }

    /**
     * Finds and collects all {@link Guild Guild} instances that contain this {@link User User} within the current {@link JDA JDA} instance.<br>
     * <p>This method is a shortcut for {@link JDA#getMutualGuilds(User...) JDA.getMutualGuilds(User)}.</p>
     *
     * @return Immutable list of all {@link Guild Guilds} that this user is a member of.
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @NotNull
    @Override
    public List<Guild> getMutualGuilds() {
        return user().getMutualGuilds();
    }

    /**
     * Returns whether or not the given user is a Bot-Account (special badge in client, some different behaviour)
     *
     * @return If the User's Account is marked as Bot
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @Override
    public boolean isBot() {
        return user().isBot();
    }

    /**
     * Returns whether or not the given user is a System account, which includes the urgent message account
     * and the community updates bot.
     *
     * @return Whether the User's account is marked as System
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @Override
    public boolean isSystem() {
        return user().isSystem();
    }

    /**
     * Returns the {@link JDA JDA} instance of this User
     *
     * @return the corresponding JDA instance
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @NotNull
    @Override
    public JDA getJDA() {
        return user().getJDA();
    }

    /**
     * Returns the {@link UserFlag UserFlags} of this user.
     *
     * @return EnumSet containing the flags of the user.
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @NotNull
    @Override
    public EnumSet<UserFlag> getFlags() {
        return user().getFlags();
    }

    /**
     * Returns the bitmask representation of the {@link UserFlag UserFlags} of this user.
     *
     * @return bitmask representation of the user's flags.
     * @throws UnsupportedOperationException If this User was created with {@link #fromId(long)}
     */
    @Override
    public int getFlagsRaw() {
        return user().getFlagsRaw();
    }

    /**
     * Retrieve a Mention for this Entity.
     * For the public {@link Role Role} (@everyone), this will return the literal string {@code "@everyone"}.
     *
     * @return A resolvable mention.
     */
    @NotNull
    @Override
    public String getAsMention() {
        return user().getAsMention();
    }

    /**
     * The Snowflake id of this entity. This is unique to every entity and will never change.
     *
     * @return Long containing the Id.
     */
    @Override
    public long getIdLong() {
        return user().getIdLong();
    }
}
