package net.ryanland.colossus.sys.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.templates.Template;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.*;
import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;
import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.api.utils.cache.MemberCacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
import net.dv8tion.jda.api.utils.concurrent.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public record ColossusGuild(Guild guild) implements Guild, ColossusDatabaseEntity<Guild> {

    @Override
    public Guild getClient() {
        return guild();
    }

    @NotNull
    @Override
    public RestAction<List<Command>> retrieveCommands() {
        return guild().retrieveCommands();
    }

    @NotNull
    @Override
    public RestAction<Command> retrieveCommandById(@NotNull String id) {
        return guild().retrieveCommandById(id);
    }

    @NotNull
    @Override
    public CommandCreateAction upsertCommand(@NotNull CommandData command) {
        return guild().upsertCommand(command);
    }

    @NotNull
    @Override
    public CommandListUpdateAction updateCommands() {
        return guild().updateCommands();
    }

    @NotNull
    @Override
    public CommandEditAction editCommandById(@NotNull String id) {
        return guild().editCommandById(id);
    }

    @NotNull
    @Override
    public RestAction<Void> deleteCommandById(@NotNull String commandId) {
        return guild().deleteCommandById(commandId);
    }

    @NotNull
    @Override
    public RestAction<List<CommandPrivilege>> retrieveCommandPrivilegesById(@NotNull String id) {
        return guild().retrieveCommandPrivilegesById(id);
    }

    @NotNull
    @Override
    public RestAction<Map<String, List<CommandPrivilege>>> retrieveCommandPrivileges() {
        return guild().retrieveCommandPrivileges();
    }

    @NotNull
    @Override
    public RestAction<List<CommandPrivilege>> updateCommandPrivilegesById(@NotNull String id, @NotNull Collection<? extends CommandPrivilege> privileges) {
        return guild().updateCommandPrivilegesById(id, privileges);
    }

    @NotNull
    @Override
    public RestAction<Map<String, List<CommandPrivilege>>> updateCommandPrivileges(@NotNull Map<String, Collection<? extends CommandPrivilege>> privileges) {
        return guild().updateCommandPrivileges(privileges);
    }

    @NotNull
    @Override
    public RestAction<EnumSet<Region>> retrieveRegions(boolean includeDeprecated) {
        return guild().retrieveRegions(includeDeprecated);
    }

    @NotNull
    @Override
    public MemberAction addMember(@NotNull String accessToken, @NotNull String userId) {
        return guild().addMember(accessToken, userId);
    }

    @Override
    public boolean isLoaded() {
        return guild().isLoaded();
    }

    @Override
    public void pruneMemberCache() {
        guild().pruneMemberCache();
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

    /**
     * @deprecated
     */
    @NotNull
    @Override
    public RestAction<String> retrieveVanityUrl() {
        return guild().retrieveVanityUrl();
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
    public Locale getLocale() {
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
        return 0;
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

    /**
     * @deprecated
     */
    @NotNull
    @Override
    public String getRegionRaw() {
        return guild().getRegionRaw();
    }

    @Override
    public boolean isMember(@NotNull User user) {
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
    public Member getMember(@NotNull User user) {
        return guild().getMember(user);
    }

    @NotNull
    @Override
    public MemberCacheView getMemberCache() {
        return guild().getMemberCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<Category> getCategoryCache() {
        return guild().getCategoryCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<StoreChannel> getStoreChannelCache() {
        return guild().getStoreChannelCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<TextChannel> getTextChannelCache() {
        return guild().getTextChannelCache();
    }

    @NotNull
    @Override
    public SortedSnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
        return guild().getVoiceChannelCache();
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
    public SnowflakeCacheView<Emote> getEmoteCache() {
        return guild().getEmoteCache();
    }

    @NotNull
    @Override
    public RestAction<List<ListedEmote>> retrieveEmotes() {
        return guild().retrieveEmotes();
    }

    @NotNull
    @Override
    public RestAction<ListedEmote> retrieveEmoteById(@NotNull String id) {
        return guild().retrieveEmoteById(id);
    }

    @NotNull
    @Override
    public RestAction<List<Ban>> retrieveBanList() {
        return guild().retrieveBanList();
    }

    @NotNull
    @Override
    public RestAction<Ban> retrieveBanById(@NotNull String userId) {
        return guild().retrieveBanById(userId);
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
    public TextChannel getDefaultChannel() {
        return guild().getDefaultChannel();
    }

    @NotNull
    @Override
    public GuildManager getManager() {
        return guild().getManager();
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
    public RestAction<Void> delete(@Nullable String mfaCode) {
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
    public RestAction<Template> createTemplate(@NotNull String name, @Nullable String description) {
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

    /**
     * @deprecated
     */
    @Override
    public boolean checkVerification() {
        return false;
    }

    /**
     * @deprecated
     */
    @Override
    public boolean isAvailable() {
        return false;
    }

    /**
     * @deprecated
     */
    @NotNull
    @Override
    public CompletableFuture<Void> retrieveMembers() {
        return guild().retrieveMembers();
    }

    @NotNull
    @Override
    public Task<Void> loadMembers(@NotNull Consumer<Member> callback) {
        return guild().loadMembers(callback);
    }

    @NotNull
    @Override
    public RestAction<Member> retrieveMemberById(long id, boolean update) {
        return guild().retrieveMemberById(id, update);
    }

    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByIds(boolean includePresence, @NotNull long... ids) {
        return guild().retrieveMembersByIds(includePresence, ids);
    }

    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByPrefix(@NotNull String prefix, int limit) {
        return guild().retrieveMembersByPrefix(prefix, limit);
    }

    @NotNull
    @Override
    public RestAction<Void> moveVoiceMember(@NotNull Member member, @Nullable VoiceChannel voiceChannel) {
        return guild().moveVoiceMember(member, voiceChannel);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyNickname(@NotNull Member member, @Nullable String nickname) {
        return guild().modifyNickname(member, nickname);
    }

    @NotNull
    @Override
    public AuditableRestAction<Integer> prune(int days, boolean wait, @NotNull Role... roles) {
        return guild().prune(days, wait, roles);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick(@NotNull Member member, @Nullable String reason) {
        return guild().kick(member, reason);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> kick(@NotNull String userId, @Nullable String reason) {
        return guild().kick(userId, reason);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> ban(@NotNull User user, int delDays, @Nullable String reason) {
        return guild().ban(user, delDays, reason);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> ban(@NotNull String userId, int delDays, @Nullable String reason) {
        return guild().ban(userId, delDays, reason);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> unban(@NotNull String userId) {
        return guild().unban(userId);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> deafen(@NotNull Member member, boolean deafen) {
        return guild().deafen(member, deafen);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> mute(@NotNull Member member, boolean mute) {
        return guild().mute(member, mute);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> addRoleToMember(@NotNull Member member, @NotNull Role role) {
        return guild().addRoleToMember(member, role);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> removeRoleFromMember(@NotNull Member member, @NotNull Role role) {
        return guild().removeRoleFromMember(member, role);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyMemberRoles(@NotNull Member member, @Nullable Collection<Role> rolesToAdd, @Nullable Collection<Role> rolesToRemove) {
        return guild().modifyMemberRoles(member, rolesToAdd, rolesToRemove);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> modifyMemberRoles(@NotNull Member member, @NotNull Collection<Role> roles) {
        return guild().modifyMemberRoles(member, roles);
    }

    @NotNull
    @Override
    public AuditableRestAction<Void> transferOwnership(@NotNull Member newOwner) {
        return guild().transferOwnership(newOwner);
    }

    @NotNull
    @Override
    public ChannelAction<TextChannel> createTextChannel(@NotNull String name, @Nullable Category parent) {
        return guild().createTextChannel(name, parent);
    }

    @NotNull
    @Override
    public ChannelAction<VoiceChannel> createVoiceChannel(@NotNull String name, @Nullable Category parent) {
        return guild().createVoiceChannel(name, parent);
    }

    @NotNull
    @Override
    public ChannelAction<StageChannel> createStageChannel(@NotNull String name, @Nullable Category parent) {
        return guild().createStageChannel(name, parent);
    }

    @NotNull
    @Override
    public ChannelAction<Category> createCategory(@NotNull String name) {
        return guild().createCategory(name);
    }

    @NotNull
    @Override
    public RoleAction createRole() {
        return guild().createRole();
    }

    @NotNull
    @Override
    public AuditableRestAction<Emote> createEmote(@NotNull String name, @NotNull Icon icon, @NotNull Role... roles) {
        return guild().createEmote(name, icon, roles);
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyCategoryPositions() {
        return guild().modifyCategoryPositions();
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyTextChannelPositions() {
        return guild().modifyTextChannelPositions();
    }

    @NotNull
    @Override
    public ChannelOrderAction modifyVoiceChannelPositions() {
        return guild().modifyVoiceChannelPositions();
    }

    @NotNull
    @Override
    public CategoryOrderAction modifyTextChannelPositions(@NotNull Category category) {
        return guild().modifyTextChannelPositions(category);
    }

    @NotNull
    @Override
    public CategoryOrderAction modifyVoiceChannelPositions(@NotNull Category category) {
        return guild().modifyVoiceChannelPositions(category);
    }

    @NotNull
    @Override
    public RoleOrderAction modifyRolePositions(boolean useAscendingOrder) {
        return guild().modifyRolePositions(useAscendingOrder);
    }

    @Override
    public long getIdLong() {
        return guild().getIdLong();
    }
}
