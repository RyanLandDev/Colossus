package net.ryanland.colossus.sys.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public record ColossusMember(Member member) implements Member, ColossusDatabaseEntity<Member> {

    @Override
    public Member getClient() {
        return member();
    }


    /**
     * The user wrapped by this Entity.
     *
     * @return {@link User User}
     */
    @NotNull
    @Override
    public User getUser() {
        return member().getUser();
    }

    /**
     * The Guild in which this Member is represented.
     *
     * @return {@link Guild Guild}
     */
    @NotNull
    @Override
    public Guild getGuild() {
        return member().getGuild();
    }

    /**
     * The Guild-Wide Permissions this PermissionHolder holds.
     * <br><u>Changes to the returned set do not affect this entity directly.</u>
     *
     * @return An EnumSet of Permissions granted to this PermissionHolder.
     */
    @NotNull
    @Override
    public EnumSet<Permission> getPermissions() {
        return member().getPermissions();
    }

    /**
     * The Permissions this PermissionHolder holds in the specified {@link GuildChannel GuildChannel}.
     * <br>Permissions returned by this may be different from {@link #getPermissions()}
     * due to the GuildChannel's {@link PermissionOverride PermissionOverrides}.
     * <br><u>Changes to the returned set do not affect this entity directly.</u>
     *
     * @param channel The {@link GuildChannel GuildChannel} of which to get Permissions for
     * @return Set of Permissions granted to this Permission Holder in the specified channel.
     * @throws IllegalArgumentException If the channel is null
     */
    @NotNull
    @Override
    public EnumSet<Permission> getPermissions(GuildChannel channel) {
        return member().getPermissions(channel);
    }

    /**
     * The explicitly granted permissions for this permission holder in the guild.
     * <br>This disregards owner and administrator privileges.
     * For a role this is identical to {@link #getPermissions()} and members have all their roles taken into consideration.
     * <br><u>Changes to the returned set do not affect this entity directly.</u>
     *
     * @return EnumSet of the explicitly granted permissions
     */
    @NotNull
    @Override
    public EnumSet<Permission> getPermissionsExplicit() {
        return member().getPermissionsExplicit();
    }

    /**
     * The explicitly granted permissions for this permission holder in the guild.
     * <br>This disregards owner and administrator privileges.
     * <br>Permissions returned by this may be different from {@link #getPermissionsExplicit()}
     * due to the GuildChannel's {@link PermissionOverride PermissionOverrides}.
     * <br><u>Changes to the returned set do not affect this entity directly.</u>
     *
     * @param channel The {@link GuildChannel GuildChannel} of which to get Permissions for
     * @return EnumSet of the explicitly granted permissions in the specified channel
     * @throws IllegalArgumentException If the channel is null
     */
    @NotNull
    @Override
    public EnumSet<Permission> getPermissionsExplicit(GuildChannel channel) {
        return member().getPermissionsExplicit(channel);
    }

    /**
     * Checks whether or not this PermissionHolder has the given {@link Permission Permissions} in the Guild.
     *
     * @param permissions Permissions to check for.
     * @return True, if all of the specified Permissions are granted to this PermissionHolder.
     * @throws IllegalArgumentException If null is provided
     */
    @Override
    public boolean hasPermission(Permission... permissions) {
        return member().hasPermission(permissions);
    }

    /**
     * Checks whether or not this PermissionHolder has the {@link Permission Permissions} in the provided
     * {@code Collection<Permission>} in the Guild.
     *
     * @param permissions Permissions to check for.
     * @return True, if all of the specified Permissions are granted to this PermissionHolder.
     * @throws IllegalArgumentException If null is provided
     * @see EnumSet EnumSet
     */
    @Override
    public boolean hasPermission(Collection<Permission> permissions) {
        return member().hasPermission(permissions);
    }

    /**
     * Checks whether or not this PermissionHolder has the given {@link Permission Permissions} in the specified GuildChannel.
     *
     * @param channel     The {@link GuildChannel GuildChannel} in which to check.
     * @param permissions Permissions to check for.
     * @return True, if all of the specified Permissions are granted to this PermissionHolder in the provided GuildChannel.
     * @throws IllegalArgumentException If null is provided
     * @see EnumSet EnumSet
     */
    @Override
    public boolean hasPermission(GuildChannel channel, Permission... permissions) {
        return member().hasPermission(channel, permissions);
    }

    /**
     * Checks whether or not this PermissionHolder has the {@link Permission Permissions} in the provided
     * {@code Collection<Permission>} in the specified GuildChannel.
     *
     * @param channel     The {@link GuildChannel GuildChannel} in which to check.
     * @param permissions Permissions to check for.
     * @return True, if all of the specified Permissions are granted to this PermissionHolder in the provided GuildChannel.
     * @throws IllegalArgumentException If null is provided
     */
    @Override
    public boolean hasPermission(GuildChannel channel, Collection<Permission> permissions) {
        return member().hasPermission(channel, permissions);
    }

    /**
     * Whether the permissions of this PermissionHolder are good enough to sync the target channel with the sync source.
     * <br>This checks what permissions would be changed by the overrides of the sync source and whether the permission holder is able to set them on the target channel.
     *
     * <p>If the permission holder had {@link Permission#MANAGE_PERMISSIONS} in an override on the target channel or {@link Permission#ADMINISTRATOR} on one of its roles, then it can set any permission on the target channel.
     * Otherwise, the permission holder can only set permissions it also has in the channel.
     *
     * @param targetChannel The target channel to check
     * @param syncSource    The sync source, for example the parent category (see {@link ICategorizableChannel#getParentCategory()})
     * @return True, if the channels can be synced
     * @throws IllegalArgumentException If either of the channels is null or not from the same guild as this permission holder
     */
    @Override
    public boolean canSync(IPermissionContainer targetChannel, IPermissionContainer syncSource) {
        return member().canSync(targetChannel, syncSource);
    }

    /**
     * Whether the permissions of this PermissionHolder are good enough to sync the target channel with any other channel.
     * <br>This checks whether the permission holder has <em>local administrator</em>.
     *
     * <p>If the permission holder had {@link Permission#MANAGE_PERMISSIONS} in an override on the target channel or {@link Permission#ADMINISTRATOR} on one of its roles, then it can set any permission on the target channel.
     *
     * @param channel The target channel to check
     * @return True, if the channel can be synced
     * @throws IllegalArgumentException If the channel is null or not from the same guild as this permission holder
     */
    @Override
    public boolean canSync(IPermissionContainer channel) {
        return member().canSync(channel);
    }

    /**
     * The JDA instance.
     *
     * @return The current JDA instance.
     */
    @NotNull
    @Override
    public JDA getJDA() {
        return member().getJDA();
    }

    /**
     * The {@link OffsetDateTime Time} this Member joined the Guild.
     * <br>If the member was loaded through a presence update (lazy loading) this will be identical
     * to the creation time of the guild. You can use {@link #hasTimeJoined()} to test whether this time
     * can be relied on.
     *
     * <p>You can use {@link Guild#retrieveMemberById(String) guild.retrieveMemberById(member.getId())}
     * to load the join time.
     *
     * @return The time at which this user has joined the guild.
     */
    @NotNull
    @Override
    public OffsetDateTime getTimeJoined() {
        return member().getTimeJoined();
    }

    /**
     * Whether this member has accurate {@link #getTimeJoined()} information.
     * <br>Discord doesn't always provide this information when we load members so we have to fallback
     * to the {@link Guild} creation time.
     *
     * <p>You can use {@link Guild#retrieveMemberById(String) guild.retrieveMemberById(member.getId())}
     * to load the join time.
     *
     * @return True, if {@link #getTimeJoined()} is accurate
     */
    @Override
    public boolean hasTimeJoined() {
        return member().hasTimeJoined();
    }

    /**
     * The time when this member boosted the guild.
     * <br>Null indicates this member is not currently boosting the guild.
     *
     * @return The boosting time, or null if the member is not boosting
     * @since 4.0.0
     */
    @Nullable
    @Override
    public OffsetDateTime getTimeBoosted() {
        return member().getTimeBoosted();
    }

    /**
     * Returns whether a member is boosting the guild or not.
     *
     * @return True, if it is boosting
     */
    @Override
    public boolean isBoosting() {
        return member().isBoosting();
    }

    /**
     * The time this Member will be released from time out.
     * <br>If this Member is not in time out, this returns {@code null}.
     * This may also return dates in the past, in which case the time out has expired.
     *
     * @return The time this Member will be released from time out or {@code null} if not in time out
     */
    @Nullable
    @Override
    public OffsetDateTime getTimeOutEnd() {
        return member().getTimeOutEnd();
    }

    /**
     * The {@link GuildVoiceState VoiceState} of this Member.
     * <br><b>This will be null when the {@link CacheFlag#VOICE_STATE} is disabled manually</b>
     *
     * <p>This can be used to get the Member's VoiceChannel using {@link GuildVoiceState#getChannel()}.
     *
     * <p>This requires {@link CacheFlag#VOICE_STATE CacheFlag.VOICE_STATE} to be enabled!
     *
     * @return {@link GuildVoiceState GuildVoiceState}
     */
    @Nullable
    @Override
    public GuildVoiceState getVoiceState() {
        return member().getVoiceState();
    }

    /**
     * The activities of the user.
     * <br>If the user does not currently have any activity, this returns an empty list.
     *
     * <p>This requires {@link CacheFlag#ACTIVITY CacheFlag.ACTIVITY} to be enabled!
     *
     * @return Immutable list of {@link Activity Activities} for the user
     */
    @NotNull
    @Override
    public List<Activity> getActivities() {
        return member().getActivities();
    }

    /**
     * Returns the {@link OnlineStatus OnlineStatus} of the User.
     * <br>If the {@link OnlineStatus OnlineStatus} is unrecognized, will return {@link OnlineStatus#UNKNOWN UNKNOWN}.
     *
     * <p>This will always return {@link OnlineStatus#OFFLINE} if {@link CacheFlag#ONLINE_STATUS CacheFlag.ONLINE_STATUS} is disabled.
     *
     * @return The current {@link OnlineStatus OnlineStatus} of the {@link User User}.
     */
    @NotNull
    @Override
    public OnlineStatus getOnlineStatus() {
        return member().getOnlineStatus();
    }

    /**
     * The platform dependent {@link OnlineStatus} of this member.
     * <br>Since a user can be connected from multiple different devices such as web and mobile,
     * discord specifies a status for each {@link ClientType}.
     *
     * <p>If a user is not online on the specified type,
     * {@link OnlineStatus#OFFLINE OFFLINE} is returned.
     *
     * <p>This requires {@link CacheFlag#CLIENT_STATUS CacheFlag.CLIENT_STATUS} to be enabled!
     *
     * @param type The type of client
     * @return The status for that specific client or OFFLINE
     * @throws IllegalArgumentException If the provided type is null
     * @since 4.0.0
     */
    @NotNull
    @Override
    public OnlineStatus getOnlineStatus(ClientType type) {
        return member().getOnlineStatus(type);
    }

    /**
     * A Set of all active {@link ClientType ClientTypes} of this Member.
     * Every {@link OnlineStatus OnlineStatus} other than {@code OFFLINE} and {@code UNKNOWN}
     * is interpreted as active.
     * Since {@code INVISIBLE} is only possible for the SelfUser, other Members will never have ClientTypes show as
     * active when actually being {@code INVISIBLE}, since they will show as {@code OFFLINE}.
     * <br>If the Member is currently not active with any Client, this returns an empty Set.
     * <br>When {@link CacheFlag#CLIENT_STATUS CacheFlag.CLIENT_STATUS} is disabled,
     * active clients will not be tracked and this will always return an empty Set.
     * <br>Since a user can be connected from multiple different devices such as web and mobile,
     * discord specifies a status for each {@link ClientType}.
     *
     * @return EnumSet of all active {@link ClientType ClientTypes}
     * @since 4.0.0
     */
    @NotNull
    @Override
    public EnumSet<ClientType> getActiveClients() {
        return member().getActiveClients();
    }

    /**
     * Returns the current nickname of this Member for the parent Guild.
     *
     * <p>This can be changed using
     * {@link Guild#modifyNickname(Member, String) modifyNickname(Member, String)}.
     *
     * @return The nickname or null, if no nickname is set.
     */
    @Nullable
    @Override
    public String getNickname() {
        return member().getNickname();
    }

    /**
     * Retrieves the Name displayed in the official Discord Client.
     *
     * @return The Nickname of this Member or the Username if no Nickname is present.
     */
    @NotNull
    @Override
    public String getEffectiveName() {
        return member().getEffectiveName();
    }

    /**
     * The Discord Id for this member's per guild avatar image.
     * If the member has not set a per guild avatar, this will return null.
     *
     * @return Possibly-null String containing the {@link Member} per guild avatar id.
     */
    @Nullable
    @Override
    public String getAvatarId() {
        return member().getAvatarId();
    }

    /**
     * The roles applied to this Member.
     * <br>The roles are ordered based on their position. The highest role being at index 0
     * and the lowest at the last index.
     *
     * <p>A Member's roles can be changed using the {@link Guild#addRoleToMember(UserSnowflake, Role)}, {@link Guild#removeRoleFromMember(UserSnowflake, Role)}, and {@link Guild#modifyMemberRoles(Member, Collection, Collection)}
     * methods in {@link Guild Guild}.
     *
     * <p><b>The Public Role ({@code @everyone}) is not included in the returned immutable list of roles
     * <br>It is implicit that every member holds the Public Role in a Guild thus it is not listed here!</b>
     *
     * @return An immutable List of {@link Role Roles} for this Member.
     * @see Guild#addRoleToMember(UserSnowflake, Role)
     * @see Guild#removeRoleFromMember(UserSnowflake, Role)
     * @see Guild#modifyMemberRoles(Member, Collection, Collection)
     */
    @NotNull
    @Override
    public List<Role> getRoles() {
        return member().getRoles();
    }

    /**
     * The {@link Color Color} of this Member's name in a Guild.
     *
     * <p>This is determined by the color of the highest role assigned to them that does not have the default color.
     * <br>If all roles have default color, this returns null.
     *
     * @return The display Color for this Member.
     * @see #getColorRaw()
     */
    @Nullable
    @Override
    public Color getColor() {
        return member().getColor();
    }

    /**
     * The raw RGB value for the color of this member.
     * <br>Defaulting to {@link Role#DEFAULT_COLOR_RAW Role.DEFAULT_COLOR_RAW}
     * if this member uses the default color (special property, it changes depending on theme used in the client)
     *
     * @return The raw RGB value or the role default
     */
    @Override
    public int getColorRaw() {
        return member().getColorRaw();
    }

    /**
     * Whether this Member can interact with the provided Member
     * (kick/ban/etc.)
     *
     * @param member The target Member to check
     * @return True, if this Member is able to interact with the specified Member
     * @throws NullPointerException     if the specified Member is null
     * @throws IllegalArgumentException if the specified Member is not from the same guild
     */
    @Override
    public boolean canInteract(Member member) {
        return member().canInteract(member);
    }

    /**
     * Whether this Member can interact with the provided {@link Role Role}
     * (kick/ban/move/modify/delete/etc.)
     *
     * <p>If this returns true this member can assign the role to other members.
     *
     * @param role The target Role to check
     * @return True, if this member is able to interact with the specified Role
     * @throws NullPointerException     if the specified Role is null
     * @throws IllegalArgumentException if the specified Role is not from the same guild
     */
    @Override
    public boolean canInteract(Role role) {
        return member().canInteract(role);
    }

    /**
     * Whether this Member can interact with the provided {@link RichCustomEmoji}
     * (use in a message)
     *
     * @param emoji The target emoji to check
     * @return True, if this Member is able to interact with the specified emoji
     * @throws NullPointerException     if the specified emoji is null
     * @throws IllegalArgumentException if the specified emoji is not from the same guild
     */
    @Override
    public boolean canInteract(RichCustomEmoji emoji) {
        return member().canInteract(emoji);
    }

    /**
     * Checks whether this member is the owner of its related {@link Guild Guild}.
     *
     * @return True, if this member is the owner of the attached Guild.
     */
    @Override
    public boolean isOwner() {
        return member().isOwner();
    }

    /**
     * Checks whether this member has passed the {@link Guild Guild's}
     * Membership Screening requirements.
     *
     * @return True, if this member hasn't passed the guild's Membership Screening requirements
     * @incubating Discord is still trying to figure this out
     * @since 4.2.1
     */
    @Override
    public boolean isPending() {
        return member().isPending();
    }

    /**
     * The default {@link BaseGuildMessageChannel BaseGuildMessageChannel} for a {@link Member Member}.
     * <br>This is the channel that the Discord client will default to opening when a Guild is opened for the first time
     * after joining the guild.
     * <br>The default channel is the channel with the highest position in which the member has
     * {@link Permission#VIEW_CHANNEL Permission.VIEW_CHANNEL} permissions. If this requirement doesn't apply for
     * any channel in the guild, this method returns {@code null}.
     *
     * @return The {@link BaseGuildMessageChannel BaseGuildMessageChannel} representing the default channel for this member
     * or null if no such channel exists.
     */
    @Nullable
    @Override
    public BaseGuildMessageChannel getDefaultChannel() {
        return member().getDefaultChannel();
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
        return member().getAsMention();
    }

    /**
     * The Snowflake id of this entity. This is unique to every entity and will never change.
     *
     * @return Long containing the Id.
     */
    @Override
    public long getIdLong() {
        return member().getIdLong();
    }
}
