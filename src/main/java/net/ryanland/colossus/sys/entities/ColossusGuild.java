package net.ryanland.colossus.sys.entities;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.sticker.*;
import net.dv8tion.jda.api.entities.templates.Template;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.PrivilegeConfig;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.privileges.IntegrationPrivilege;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.managers.GuildStickerManager;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.*;
import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.OrderAction;
import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;
import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.api.requests.restaction.pagination.BanPaginationAction;
import net.dv8tion.jda.api.requests.restaction.pagination.PaginationAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.cache.MemberCacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
import net.dv8tion.jda.api.utils.concurrent.Task;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public record ColossusGuild(Guild guild) implements Guild, ColossusDatabaseEntity {

    @Override
    public String getStockName() {
        return "guilds";
    }

    @Override
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(Map.of("_guild_id", guild.getId()));
    }

    public String getPrefix() {
        String prefix = getValue("prefix");
        if (prefix != null) return prefix;
        else return Colossus.getConfig().getPrefix();
    }

    
    @NotNull
    @Override
    public RestAction<List<Command>> retrieveCommands() {
        return guild().retrieveCommands();
    }

    
    @NotNull
    @Override
    public RestAction<List<Command>> retrieveCommands(boolean withLocalizations) {
        return guild().retrieveCommands(withLocalizations);
    }

    
    @NotNull
    @Override
    public RestAction<Command> retrieveCommandById(String id) {
        return guild().retrieveCommandById(id);
    }

    
    @NotNull
    @Override
    public RestAction<Command> upsertCommand(CommandData command) {
        return guild().upsertCommand(command);
    }

    
    @NotNull
    @Override
    public CommandListUpdateAction updateCommands() {
        return guild().updateCommands();
    }

    
    @NotNull
    @Override
    public CommandEditAction editCommandById(String id) {
        return guild().editCommandById(id);
    }

    
    @NotNull
    @Override
    public RestAction<Void> deleteCommandById(String commandId) {
        return guild().deleteCommandById(commandId);
    }

    
    @NotNull
    @Override
    public RestAction<List<IntegrationPrivilege>> retrieveIntegrationPrivilegesById(String targetId) {
        return guild().retrieveIntegrationPrivilegesById(targetId);
    }

    
    @NotNull
    @Override
    public RestAction<PrivilegeConfig> retrieveCommandPrivileges() {
        return guild().retrieveCommandPrivileges();
    }

    
    @NotNull
    @Override
    public RestAction<EnumSet<Region>> retrieveRegions(boolean includeDeprecated) {
        return guild().retrieveRegions(includeDeprecated);
    }

    
    @NotNull
    @Override
    public MemberAction addMember(String accessToken, UserSnowflake user) {
        return guild().addMember(accessToken, user);
    }

    
    @Override
    public boolean isLoaded() {
        return guild().isLoaded();
    }

    
    @Override
    public void pruneMemberCache() {

    }

    
    @Override
    public boolean unloadMember(long userId) {
        return guild().unloadMember(userId);
    }

    
    @Override
    public int getMemberCount() {
        return guild().getMemberCount();
    }

    
    @NotNull
    @Override
    public String getName() {
        return guild().getName();
    }

    
    @Nullable
    @Override
    public String getIconId() {
        return guild().getIconId();
    }

    
    @NotNull
    @Override
    public Set<String> getFeatures() {
        return guild().getFeatures();
    }

    
    @Nullable
    @Override
    public String getSplashId() {
        return guild().getSplashId();
    }

    
    @Nullable
    @Override
    public String getVanityCode() {
        return guild().getVanityCode();
    }

    
    @NotNull
    @Override
    public RestAction<VanityInvite> retrieveVanityInvite() {
        return guild().retrieveVanityInvite();
    }

    
    @Nullable
    @Override
    public String getDescription() {
        return guild().getDescription();
    }

    
    @NotNull
    @Override
    public DiscordLocale getLocale() {
        return guild().getLocale();
    }

    
    @Nullable
    @Override
    public String getBannerId() {
        return guild().getBannerId();
    }

    
    @NotNull
    @Override
    public BoostTier getBoostTier() {
        return guild().getBoostTier();
    }

    
    @Override
    public int getBoostCount() {
        return guild().getBoostCount();
    }

    
    @NotNull
    @Override
    public List<Member> getBoosters() {
        return guild().getBoosters();
    }

    
    @Override
    public int getMaxMembers() {
        return guild().getMaxMembers();
    }

    
    @Override
    public int getMaxPresences() {
        return guild().getMaxPresences();
    }

    
    @NotNull
    @Override
    public RestAction<MetaData> retrieveMetaData() {
        return guild().retrieveMetaData();
    }

    
    @Nullable
    @Override
    public VoiceChannel getAfkChannel() {
        return guild().getAfkChannel();
    }

    
    @Nullable
    @Override
    public TextChannel getSystemChannel() {
        return guild().getSystemChannel();
    }

    
    @Nullable
    @Override
    public TextChannel getRulesChannel() {
        return guild().getRulesChannel();
    }

    
    @Nullable
    @Override
    public TextChannel getCommunityUpdatesChannel() {
        return guild().getCommunityUpdatesChannel();
    }

    
    @Nullable
    @Override
    public Member getOwner() {
        return guild().getOwner();
    }

    
    @Override
    public long getOwnerIdLong() {
        return guild().getOwnerIdLong();
    }

    
    @NotNull
    @Override
    public Timeout getAfkTimeout() {
        return guild().getAfkTimeout();
    }

    
    @Override
    public boolean isMember(UserSnowflake user) {
        return guild().isMember(user);
    }

    
    @NotNull
    @Override
    public Member getSelfMember() {
        return guild().getSelfMember();
    }

    
    @NotNull
    @Override
    public NSFWLevel getNSFWLevel() {
        return guild().getNSFWLevel();
    }

    
    @Nullable
    @Override
    public Member getMember(UserSnowflake user) {
        return guild().getMember(user);
    }

    
    @NotNull
    @Override
    public MemberCacheView getMemberCache() {
        return guild().getMemberCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<StageChannel> getStageChannelCache() {
        return guild().getStageChannelCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<ThreadChannel> getThreadChannelCache() {
        return guild().getThreadChannelCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<Category> getCategoryCache() {
        return guild().getCategoryCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<TextChannel> getTextChannelCache() {
        return guild().getTextChannelCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<NewsChannel> getNewsChannelCache() {
        return guild().getNewsChannelCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
        return guild().getVoiceChannelCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<ForumChannel> getForumChannelCache() {
        return guild().getForumChannelCache();
    }

    
    @NotNull
    @Override
    public List<GuildChannel> getChannels(boolean includeHidden) {
        return guild().getChannels(includeHidden);
    }

    
    @NotNull
    @Override
    public SortedSnowflakeCacheView<Role> getRoleCache() {
        return guild().getRoleCache();
    }

    
    @NotNull
    @Override
    public SnowflakeCacheView<RichCustomEmoji> getEmojiCache() {
        return guild().getEmojiCache();
    }

    
    @NotNull
    @Override
    public SnowflakeCacheView<GuildSticker> getStickerCache() {
        return guild().getStickerCache();
    }

    
    @NotNull
    @Override
    public RestAction<List<RichCustomEmoji>> retrieveEmojis() {
        return guild().retrieveEmojis();
    }

    
    @NotNull
    @Override
    public RestAction<RichCustomEmoji> retrieveEmojiById(String id) {
        return guild().retrieveEmojiById(id);
    }

    
    @NotNull
    @Override
    public RestAction<List<GuildSticker>> retrieveStickers() {
        return guild().retrieveStickers();
    }

    
    @NotNull
    @Override
    public RestAction<GuildSticker> retrieveSticker(StickerSnowflake sticker) {
        return guild().retrieveSticker(sticker);
    }

    
    @NotNull
    @Override
    public GuildStickerManager editSticker(StickerSnowflake sticker) {
        return guild().editSticker(sticker);
    }

    @NotNull
    @Override
    public BanPaginationAction retrieveBanList() {
        return guild().retrieveBanList();
    }

    @NotNull
    @Override
    public RestAction<Ban> retrieveBan(@NotNull UserSnowflake user) {
        return guild().retrieveBan(user);
    }

    
    @NotNull
    @Override
    public RestAction<Integer> retrievePrunableMemberCount(int days) {
        return guild().retrievePrunableMemberCount(days);
    }

    
    @NotNull
    @Override
    public Role getPublicRole() {
        return guild().getPublicRole();
    }

    @Nullable
    @Override
    public DefaultGuildChannelUnion getDefaultChannel() {
        return guild().getDefaultChannel();
    }

    
    @NotNull
    @Override
    public GuildManager getManager() {
        return guild().getManager();
    }

    
    @Override
    public boolean isBoostProgressBarEnabled() {
        return guild().isBoostProgressBarEnabled();
    }

    
    @NotNull
    @Override
    public AuditLogPaginationAction retrieveAuditLogs() {
        return guild().retrieveAuditLogs();
    }

    
    @NotNull
    @Override
    public RestAction<Void> leave() {
        return guild().leave();
    }

    
    @NotNull
    @Override
    public RestAction<Void> delete() {
        return guild().delete();
    }

    
    @NotNull
    @Override
    public RestAction<Void> delete(String mfaCode) {
        return guild().delete(mfaCode);
    }

    
    @NotNull
    @Override
    public AudioManager getAudioManager() {
        return guild().getAudioManager();
    }

    
    @NotNull
    @Override
    public Task<Void> requestToSpeak() {
        return guild().requestToSpeak();
    }

    
    @NotNull
    @Override
    public Task<Void> cancelRequestToSpeak() {
        return guild().cancelRequestToSpeak();
    }
    
    @NotNull
    @Override
    public JDA getJDA() {
        return guild().getJDA();
    }
    @NotNull
    @Override
    public RestAction<List<Invite>> retrieveInvites() {
        return guild().retrieveInvites();
    }
    
    @NotNull
    @Override
    public RestAction<List<Template>> retrieveTemplates() {
        return guild().retrieveTemplates();
    }
    
    @NotNull
    @Override
    public RestAction<Template> createTemplate(String name, String description) {
        return guild().createTemplate(name, description);
    }
    
    @NotNull
    @Override
    public RestAction<List<Webhook>> retrieveWebhooks() {
        return guild().retrieveWebhooks();
    }
    
    @NotNull
    @Override
    public List<GuildVoiceState> getVoiceStates() {
        return guild().getVoiceStates();
    }
    
    @NotNull
    @Override
    public VerificationLevel getVerificationLevel() {
        return guild().getVerificationLevel();
    }
    
    @NotNull
    @Override
    public NotificationLevel getDefaultNotificationLevel() {
        return guild().getDefaultNotificationLevel();
    }
    
    @NotNull
    @Override
    public MFALevel getRequiredMFALevel() {
        return guild().getRequiredMFALevel();
    }
    
    @NotNull
    @Override
    public ExplicitContentLevel getExplicitContentLevel() {
        return guild().getExplicitContentLevel();
    }
    
    @NotNull
    @Override
    public Task<Void> loadMembers(Consumer<Member> callback) {
        return guild().loadMembers(callback);
    }
    
    @NotNull
    @Override
    public CacheRestAction<Member> retrieveMemberById(long id) {
        return guild().retrieveMemberById(id);
    }
    
    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByIds(boolean includePresence, long... ids) {
        return guild().retrieveMembersByIds(includePresence, ids);
    }

    
    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByPrefix(String prefix, int limit) {
        return guild().retrieveMembersByPrefix(prefix, limit);
    }

    @NotNull
    @Override
    public RestAction<List<ThreadChannel>> retrieveActiveThreads() {
        return guild().retrieveActiveThreads();
    }

    /**
     * Used to move a {@link Member Member} from one {@link AudioChannel AudioChannel}
     * to another {@link AudioChannel AudioChannel}.
     * <br>As a note, you cannot move a Member that isn't already in a AudioChannel. Also they must be in a AudioChannel
     * in the same Guild as the one that you are moving them to.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be moved due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The {@link Permission#VIEW_CHANNEL VIEW_CHANNEL} permission was removed</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_CHANNEL UNKNOWN_CHANNEL}
     *     <br>The specified channel was deleted before finishing the task</li>
     * </ul>
     *
     * @param member       The {@link Member Member} that you are moving.
     * @param audioChannel The destination {@link AudioChannel AudioChannel} to which the member is being
     *                     moved to. Or null to perform a voice kick.
     * @return {@link RestAction RestAction}
     * @throws IllegalStateException           If the Member isn't currently in a AudioChannel in this Guild, or {@link CacheFlag#VOICE_STATE} is disabled.
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If the provided member is {@code null}</li>
     *                                                     <li>If the provided Member isn't part of this {@link Guild Guild}</li>
     *                                                     <li>If the provided AudioChannel isn't part of this {@link Guild Guild}</li>
     *                                                 </ul>
     * @throws InsufficientPermissionException <ul>
     *                                                     <li>If this account doesn't have {@link Permission#VOICE_MOVE_OTHERS}
     *                                                         in the AudioChannel that the Member is currently in.</li>
     *                                                     <li>If this account <b>AND</b> the Member being moved don't have
     *                                                         {@link Permission#VOICE_CONNECT} for the destination AudioChannel.</li>
     *                                                 </ul>
     */
    @NotNull
    @Override
    public RestAction<Void> moveVoiceMember(Member member, AudioChannel audioChannel) {
        return guild().moveVoiceMember(member, audioChannel);
    }

    /**
     * Changes the Member's nickname in this guild.
     * The nickname is visible to all members of this guild.
     *
     * <p>To change the nickname for the currently logged in account
     * only the Permission {@link Permission#NICKNAME_CHANGE NICKNAME_CHANGE} is required.
     * <br>To change the nickname of <b>any</b> {@link Member Member} for this {@link Guild Guild}
     * the Permission {@link Permission#NICKNAME_MANAGE NICKNAME_MANAGE} is required.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The nickname of the target Member is not modifiable due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param member   The {@link Member Member} for which the nickname should be changed.
     * @param nickname The new nickname of the {@link Member Member}, provide {@code null} or an
     *                 empty String to reset the nickname
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws IllegalArgumentException        If the specified {@link Member Member}
     *                                         is not from the same {@link Guild Guild}.
     *                                         Or if the provided member is {@code null}
     * @throws InsufficientPermissionException <ul>
     *                                                     <li>If attempting to set nickname for self and the logged in account has neither {@link Permission#NICKNAME_CHANGE}
     *                                                         or {@link Permission#NICKNAME_MANAGE}</li>
     *                                                     <li>If attempting to set nickname for another member and the logged in account does not have {@link Permission#NICKNAME_MANAGE}</li>
     *                                                 </ul>
     * @throws HierarchyException              If attempting to set nickname for another member and the logged in account cannot manipulate the other user due to permission hierarchy position.
     *                                         <br>See {@link Member#canInteract(Member)}
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> modifyNickname(Member member, String nickname) {
        return guild().modifyNickname(member, nickname);
    }

    @NotNull
    @Override
    public AuditableRestAction<Integer> prune(int days, boolean wait, Role... roles) {
        return guild().prune(days, wait, roles);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick(@NotNull UserSnowflake user) {
        return guild().kick(user);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> ban(@NotNull UserSnowflake user, int deletionTimeframe, @NotNull TimeUnit unit) {
        return guild().ban(user, deletionTimeframe, unit);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> unban(UserSnowflake user) {
        return guild().unban(user);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> timeoutUntil(UserSnowflake user, TemporalAccessor temporal) {
        return guild().timeoutUntil(user, temporal);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> removeTimeout(UserSnowflake user) {
        return guild().removeTimeout(user);
    }

    /**
     * Sets the Guild Deafened state of the {@link Member Member} based on the provided
     * boolean.
     *
     * <p><b>Note:</b> The Member's {@link GuildVoiceState#isGuildDeafened() GuildVoiceState.isGuildDeafened()} value won't change
     * until JDA receives the {@link GuildVoiceGuildDeafenEvent GuildVoiceGuildDeafenEvent} event related to this change.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be deafened due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     *
     *     <li>{@link ErrorResponse#USER_NOT_CONNECTED USER_NOT_CONNECTED}
     *     <br>The specified Member is not connected to a voice channel</li>
     * </ul>
     *
     * @param user   The {@link UserSnowflake} who's {@link GuildVoiceState} to change.
     *               This can be a member or user instance or {@link User#fromId(long)}.
     * @param deafen Whether this {@link Member Member} should be deafened or undeafened.
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#VOICE_DEAF_OTHERS} permission.
     * @throws IllegalArgumentException        If the provided user is null.
     * @throws IllegalStateException           If the provided user is not currently connected to a voice channel.
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> deafen(UserSnowflake user, boolean deafen) {
        return guild().deafen(user, deafen);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> mute(UserSnowflake user, boolean mute) {
        return guild().mute(user, mute);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> addRoleToMember(UserSnowflake user, Role role) {
        return guild().addRoleToMember(user, role);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> removeRoleFromMember(UserSnowflake user, Role role) {
        return guild().removeRoleFromMember(user, role);
    }
    
    @NotNull
    @Override
    public AuditableRestAction<Void> modifyMemberRoles(Member member, Collection<Role> rolesToAdd, Collection<Role> rolesToRemove) {
        return guild().modifyMemberRoles(member, rolesToAdd, rolesToRemove);
    }

    /**
     * Modifies the complete {@link Role Role} set of the specified {@link Member Member}
     * <br>The provided roles will replace all current Roles of the specified Member.
     *
     * <p><u>The new roles <b>must not</b> contain the Public Role of the Guild</u>
     *
     * <h4>Warning</h4>
     * <b>This may <u>not</u> be used together with any other role add/remove/modify methods for the same Member
     * within one event listener cycle! The changes made by this require cache updates which are triggered by
     * lifecycle events which are received later. This may only be called again once the specific Member has been updated
     * by a {@link GenericGuildMemberEvent GenericGuildMemberEvent} targeting the same Member.</b>
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The Members Roles could not be modified due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The target Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * <h4>Example</h4>
     * <pre>{@code
     * public static void makeModerator(Member member) {
     *     Guild guild = member.getGuild();
     *     List<Role> roles = new ArrayList<>(member.getRoles()); // modifiable copy
     *     List<Role> modRoles = guild.getRolesByName("moderator", true); // get roles with name "moderator"
     *     roles.addAll(modRoles); // add new roles
     *     // update the member with new roles
     *     guild.modifyMemberRoles(member, roles).queue();
     * }
     * }</pre>
     *
     * @param member A {@link Member Member} of which to override the Roles of
     * @param roles  New collection of {@link Role Roles} for the specified Member
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#MANAGE_ROLES Permission.MANAGE_ROLES}
     * @throws HierarchyException              If the provided roles are higher in the Guild's hierarchy
     *                                         and thus cannot be modified by the currently logged in account
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If any of the provided arguments is {@code null}</li>
     *                                                     <li>If any of the provided arguments is not from this Guild</li>
     *                                                     <li>If any of the specified {@link Role Roles} is managed</li>
     *                                                     <li>If any of the specified {@link Role Roles} is the {@code Public Role} of this Guild</li>
     *                                                 </ul>
     * @see #modifyMemberRoles(Member, Collection)
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> modifyMemberRoles(Member member, Collection<Role> roles) {
        return guild().modifyMemberRoles(member, roles);
    }

    /**
     * Transfers the Guild ownership to the specified {@link Member Member}
     * <br>Only available if the currently logged in account is the owner of this Guild
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The currently logged in account lost ownership before completing the task</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The target Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param newOwner Not-null Member to transfer ownership to
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws PermissionException      If the currently logged in account is not the owner of this Guild
     * @throws IllegalArgumentException <ul>
     *                                              <li>If the specified Member is {@code null} or not from the same Guild</li>
     *                                              <li>If the specified Member already is the Guild owner</li>
     *                                              <li>If the specified Member is a bot account ({@link AccountType#BOT AccountType.BOT})</li>
     *                                          </ul>
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> transferOwnership(Member newOwner) {
        return guild().transferOwnership(newOwner);
    }

    /**
     * Creates a new {@link TextChannel TextChannel} in this Guild.
     * For this to be successful, the logged in account has to have the {@link Permission#MANAGE_CHANNEL MANAGE_CHANNEL} Permission
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The channel could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MAX_CHANNELS MAX_CHANNELS}
     *     <br>The maximum number of channels were exceeded</li>
     * </ul>
     *
     * @param name   The name of the TextChannel to create
     * @param parent The optional parent category for this channel, or null
     * @return A specific {@link ChannelAction ChannelAction}
     * <br>This action allows to set fields for the new TextChannel before creating it
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MANAGE_CHANNEL} permission
     * @throws IllegalArgumentException        If the provided name is {@code null} or empty or greater than 100 characters in length;
     *                                         or the provided parent is not in the same guild.
     */
    @NotNull
    @Override
    public ChannelAction<TextChannel> createTextChannel(String name, Category parent) {
        return guild().createTextChannel(name, parent);
    }

    /**
     * Creates a new {@link NewsChannel NewsChannel} in this Guild.
     * For this to be successful, the logged in account has to have the {@link Permission#MANAGE_CHANNEL MANAGE_CHANNEL} Permission
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The channel could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MAX_CHANNELS MAX_CHANNELS}
     *     <br>The maximum number of channels were exceeded</li>
     * </ul>
     *
     * @param name   The name of the NewsChannel to create
     * @param parent The optional parent category for this channel, or null
     * @return A specific {@link ChannelAction ChannelAction}
     * <br>This action allows to set fields for the new NewsChannel before creating it
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MANAGE_CHANNEL} permission
     * @throws IllegalArgumentException        If the provided name is {@code null} or empty or greater than 100 characters in length;
     *                                         or the provided parent is not in the same guild.
     */
    @NotNull
    @Override
    public ChannelAction<NewsChannel> createNewsChannel(String name, Category parent) {
        return guild().createNewsChannel(name, parent);
    }

    /**
     * Creates a new {@link VoiceChannel VoiceChannel} in this Guild.
     * For this to be successful, the logged in account has to have the {@link Permission#MANAGE_CHANNEL MANAGE_CHANNEL} Permission.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The channel could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MAX_CHANNELS MAX_CHANNELS}
     *     <br>The maximum number of channels were exceeded</li>
     * </ul>
     *
     * @param name   The name of the VoiceChannel to create
     * @param parent The optional parent category for this channel, or null
     * @return A specific {@link ChannelAction ChannelAction}
     * <br>This action allows to set fields for the new VoiceChannel before creating it
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MANAGE_CHANNEL} permission
     * @throws IllegalArgumentException        If the provided name is {@code null} or empty or greater than 100 characters in length;
     *                                         or the provided parent is not in the same guild.
     */
    @NotNull
    @Override
    public ChannelAction<VoiceChannel> createVoiceChannel(String name, Category parent) {
        return guild().createVoiceChannel(name, parent);
    }

    /**
     * Creates a new {@link StageChannel StageChannel} in this Guild.
     * For this to be successful, the logged in account has to have the {@link Permission#MANAGE_CHANNEL MANAGE_CHANNEL} Permission.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The channel could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MAX_CHANNELS MAX_CHANNELS}
     *     <br>The maximum number of channels were exceeded</li>
     * </ul>
     *
     * @param name   The name of the StageChannel to create
     * @param parent The optional parent category for this channel, or null
     * @return A specific {@link ChannelAction ChannelAction}
     * <br>This action allows to set fields for the new StageChannel before creating it
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MANAGE_CHANNEL} permission
     * @throws IllegalArgumentException        If the provided name is {@code null} or empty or greater than 100 characters in length;
     *                                         or the provided parent is not in the same guild.
     */
    @NotNull
    @Override
    public ChannelAction<StageChannel> createStageChannel(String name, Category parent) {
        return guild().createStageChannel(name, parent);
    }

    @NotNull
    @Override
    public ChannelAction<ForumChannel> createForumChannel(@NotNull String name, @Nullable Category parent) {
        return guild().createForumChannel(name, parent);
    }

    /**
     * Creates a new {@link Category Category} in this Guild.
     * For this to be successful, the logged in account has to have the {@link Permission#MANAGE_CHANNEL MANAGE_CHANNEL} Permission.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The channel could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MAX_CHANNELS MAX_CHANNELS}
     *     <br>The maximum number of channels were exceeded</li>
     * </ul>
     *
     * @param name The name of the Category to create
     * @return A specific {@link ChannelAction ChannelAction}
     * <br>This action allows to set fields for the new Category before creating it
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MANAGE_CHANNEL} permission
     * @throws IllegalArgumentException        If the provided name is {@code null} or empty or greater than 100 characters in length
     */
    @NotNull
    @Override
    public ChannelAction<Category> createCategory(String name) {
        return guild().createCategory(name);
    }

    /**
     * Creates a new {@link Role Role} in this Guild.
     * <br>It will be placed at the bottom (just over the Public Role) to avoid permission hierarchy conflicts.
     * <br>For this to be successful, the logged in account has to have the {@link Permission#MANAGE_ROLES MANAGE_ROLES} Permission
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The role could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MAX_ROLES_PER_GUILD MAX_ROLES_PER_GUILD}
     *     <br>There are too many roles in this Guild</li>
     * </ul>
     *
     * @return {@link RoleAction RoleAction}
     * <br>Creates a new role with previously selected field values
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MANAGE_ROLES} Permission
     */
    @NotNull
    @Override
    public RoleAction createRole() {
        return guild().createRole();
    }

    /**
     * Creates a new {@link RichCustomEmoji} in this Guild.
     * <br>If one or more Roles are specified the new emoji will only be available to Members with any of the specified Roles (see {@link Member#canInteract(RichCustomEmoji)})
     * <br>For this to be successful, the logged in account has to have the {@link Permission#MANAGE_EMOJIS_AND_STICKERS MANAGE_EMOJIS_AND_STICKERS} Permission.
     *
     * <p><b><u>Unicode emojis are not included as {@link RichCustomEmoji}!</u></b>
     *
     * <p>Note that a guild is limited to 50 normal and 50 animated emojis by default.
     * Some guilds are able to add additional emojis beyond this limitation due to the
     * {@code MORE_EMOJI} feature (see {@link Guild#getFeatures() Guild.getFeatures()}).
     * <br>Due to simplicity we do not check for these limits.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The emoji could not be created due to a permission discrepancy</li>
     * </ul>
     *
     * @param name  The name for the new emoji
     * @param icon  The {@link Icon} for the new emoji
     * @param roles The {@link Role Roles} the new emoji should be restricted to
     *              <br>If no roles are provided the emoji will be available to all Members of this Guild
     * @return {@link AuditableRestAction AuditableRestAction} - Type: {@link RichCustomEmoji}
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MANAGE_EMOJIS_AND_STICKERS MANAGE_EMOJIS_AND_STICKERS} Permission
     */
    @NotNull
    @Override
    public AuditableRestAction<RichCustomEmoji> createEmoji(String name, Icon icon, Role... roles) {
        return guild().createEmoji(name, icon, roles);
    }

    /**
     * Creates a new {@link GuildSticker} in this Guild.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#INVALID_FILE_UPLOADED INVALID_FILE_UPLOADED}
     *     <br>The sticker file asset is not in a supported file format</li>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The sticker could not be created due to a permission discrepancy</li>
     * </ul>
     *
     * @param name        The sticker name (2-30 characters)
     * @param description The sticker description (2-100 characters, or empty)
     * @param file        The sticker file containing the asset (png/apng/lottie) with valid file extension (png or json)
     * @param tags        The tags to use for auto-suggestions (Up to 200 characters in total)
     * @return {@link AuditableRestAction} - Type: {@link GuildSticker}
     * @throws InsufficientPermissionException If the currently logged in account does not have the {@link Permission#MANAGE_EMOJIS_AND_STICKERS MANAGE_EMOJIS_AND_STICKERS} permission
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If the name is not between 2 and 30 characters long</li>
     *                                                     <li>If the description is more than 100 characters long or exactly 1 character long</li>
     *                                                     <li>If the asset file is null or of an invalid format (must be PNG or LOTTIE)</li>
     *                                                     <li>If anything is {@code null}</li>
     *                                                 </ul>
     */
    @NotNull
    @Override
    public AuditableRestAction<GuildSticker> createSticker(String name, String description, FileUpload file, Collection<String> tags) {
        return guild().createSticker(name, description, file, tags);
    }

    /**
     * Deletes a sticker from the guild.
     *
     * <p>The returned {@link RestAction RestAction} can encounter the following Discord errors:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_STICKER UNKNOWN_STICKER}
     *     <br>Occurs when the provided id does not refer to a sticker known by Discord.</li>
     * </ul>
     *
     * @param id
     * @return {@link AuditableRestAction}
     * @throws IllegalStateException           If null is provided
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#MANAGE_EMOJIS_AND_STICKERS MANAGE_EMOJIS_AND_STICKERS} in the guild.
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> deleteSticker(StickerSnowflake id) {
        return guild().deleteSticker(id);
    }

    /**
     * Modifies the positional order of {@link Guild#getCategories() Guild.getCategories()}
     * using a specific {@link RestAction RestAction} extension to allow moving Channels
     * {@link OrderAction#moveUp(int) up}/{@link OrderAction#moveDown(int) down}
     * or {@link OrderAction#moveTo(int) to} a specific position.
     * <br>This uses <b>ascending</b> order with a 0 based index.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_CHANNEL UNNKOWN_CHANNEL}
     *     <br>One of the channels has been deleted before the completion of the task</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The currently logged in account was removed from the Guild</li>
     * </ul>
     *
     * @return {@link ChannelOrderAction ChannelOrderAction} - Type: {@link Category Category}
     */
    @NotNull
    @Override
    public ChannelOrderAction modifyCategoryPositions() {
        return guild().modifyCategoryPositions();
    }

    /**
     * Modifies the positional order of {@link Guild#getTextChannels() Guild.getTextChannels()}
     * using a specific {@link RestAction RestAction} extension to allow moving Channels
     * {@link OrderAction#moveUp(int) up}/{@link OrderAction#moveDown(int) down}
     * or {@link OrderAction#moveTo(int) to} a specific position.
     * <br>This uses <b>ascending</b> order with a 0 based index.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_CHANNEL UNNKOWN_CHANNEL}
     *     <br>One of the channels has been deleted before the completion of the task</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The currently logged in account was removed from the Guild</li>
     * </ul>
     *
     * @return {@link ChannelOrderAction ChannelOrderAction} - Type: {@link TextChannel TextChannel}
     */
    @NotNull
    @Override
    public ChannelOrderAction modifyTextChannelPositions() {
        return guild().modifyTextChannelPositions();
    }

    /**
     * Modifies the positional order of {@link Guild#getVoiceChannels() Guild.getVoiceChannels()}
     * using a specific {@link RestAction RestAction} extension to allow moving Channels
     * {@link OrderAction#moveUp(int) up}/{@link OrderAction#moveDown(int) down}
     * or {@link OrderAction#moveTo(int) to} a specific position.
     * <br>This uses <b>ascending</b> order with a 0 based index.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_CHANNEL UNNKOWN_CHANNEL}
     *     <br>One of the channels has been deleted before the completion of the task</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The currently logged in account was removed from the Guild</li>
     * </ul>
     *
     * @return {@link ChannelOrderAction ChannelOrderAction} - Type: {@link VoiceChannel VoiceChannel}
     */
    @NotNull
    @Override
    public ChannelOrderAction modifyVoiceChannelPositions() {
        return guild().modifyVoiceChannelPositions();
    }

    /**
     * Modifies the positional order of {@link Category#getTextChannels() Category#getTextChannels()}
     * using an extension of {@link ChannelOrderAction ChannelOrderAction}
     * specialized for ordering the nested {@link TextChannel TextChannels} of this
     * {@link Category Category}.
     * <br>Like {@code ChannelOrderAction}, the returned {@link CategoryOrderAction CategoryOrderAction}
     * can be used to move TextChannels {@link OrderAction#moveUp(int) up},
     * {@link OrderAction#moveDown(int) down}, or
     * {@link OrderAction#moveTo(int) to} a specific position.
     * <br>This uses <b>ascending</b> order with a 0 based index.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_CHANNEL UNNKOWN_CHANNEL}
     *     <br>One of the channels has been deleted before the completion of the task.</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The currently logged in account was removed from the Guild.</li>
     * </ul>
     *
     * @param category The {@link Category Category} to order
     *                 {@link TextChannel TextChannels} from.
     * @return {@link CategoryOrderAction CategoryOrderAction} - Type: {@link TextChannel TextChannel}
     */
    @NotNull
    @Override
    public CategoryOrderAction modifyTextChannelPositions(Category category) {
        return guild().modifyTextChannelPositions(category);
    }

    /**
     * Modifies the positional order of {@link Category#getVoiceChannels() Category#getVoiceChannels()}
     * using an extension of {@link ChannelOrderAction ChannelOrderAction}
     * specialized for ordering the nested {@link VoiceChannel VoiceChannels} of this
     * {@link Category Category}.
     * <br>Like {@code ChannelOrderAction}, the returned {@link CategoryOrderAction CategoryOrderAction}
     * can be used to move VoiceChannels {@link OrderAction#moveUp(int) up},
     * {@link OrderAction#moveDown(int) down}, or
     * {@link OrderAction#moveTo(int) to} a specific position.
     * <br>This uses <b>ascending</b> order with a 0 based index.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_CHANNEL UNNKOWN_CHANNEL}
     *     <br>One of the channels has been deleted before the completion of the task.</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The currently logged in account was removed from the Guild.</li>
     * </ul>
     *
     * @param category The {@link Category Category} to order
     *                 {@link VoiceChannel VoiceChannels} from.
     * @return {@link CategoryOrderAction CategoryOrderAction} - Type: {@link VoiceChannel VoiceChannels}
     */
    @NotNull
    @Override
    public CategoryOrderAction modifyVoiceChannelPositions(Category category) {
        return guild().modifyVoiceChannelPositions(category);
    }

    /**
     * Modifies the positional order of {@link Guild#getRoles() Guild.getRoles()}
     * using a specific {@link RestAction RestAction} extension to allow moving Roles
     * {@link OrderAction#moveUp(int) up}/{@link OrderAction#moveDown(int) down}
     * or {@link OrderAction#moveTo(int) to} a specific position.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_ROLE UNKNOWN_ROLE}
     *     <br>One of the roles was deleted before the completion of the task</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The currently logged in account was removed from the Guild</li>
     * </ul>
     *
     * @param useAscendingOrder Defines the ordering of the OrderAction. If {@code false}, the OrderAction will be in the ordering
     *                          defined by Discord for roles, which is Descending. This means that the highest role appears at index {@code 0}
     *                          and the lowest role at index {@code n - 1}. Providing {@code true} will result in the ordering being
     *                          in ascending order, with the lower role at index {@code 0} and the highest at index {@code n - 1}.
     *                          <br>As a note: {@link Member#getRoles() Member.getRoles()}
     *                          and {@link Guild#getRoles() Guild.getRoles()} are both in descending order.
     * @return {@link RoleOrderAction RoleOrderAction}
     */
    @NotNull
    @Override
    public RoleOrderAction modifyRolePositions(boolean useAscendingOrder) {
        return guild().modifyRolePositions(useAscendingOrder);
    }

    /**
     * The Snowflake id of this entity. This is unique to every entity and will never change.
     *
     * @return Long containing the Id.
     */
    @Override
    public long getIdLong() {
        return guild().getIdLong();
    }
}