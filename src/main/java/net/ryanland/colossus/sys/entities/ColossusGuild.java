package net.ryanland.colossus.sys.entities;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.*;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public record ColossusGuild(Guild guild) implements Guild, ColossusDatabaseEntity<Guild> {

    @Override
    public Guild getClient() {
        return guild();
    }

    /**
     * Retrieves the list of guild commands.
     * <br>This list does not include global commands! Use {@link JDA#retrieveCommands()} for global commands.
     *
     * @return {@link RestAction} - Type: {@link List} of {@link Command}
     */
    @NotNull
    @Override
    public RestAction<List<Command>> retrieveCommands() {
        return guild().retrieveCommands();
    }

    /**
     * Retrieves the existing {@link Command} instance by id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * @param id The command id
     * @return {@link RestAction} - Type: {@link Command}
     * @throws IllegalArgumentException If the provided id is not a valid snowflake
     */
    @NotNull
    @Override
    public RestAction<Command> retrieveCommandById(String id) {
        return guild().retrieveCommandById(id);
    }

    /**
     * Creates or updates a command.
     * <br>If a command with the same name exists, it will be replaced.
     * This operation is idempotent.
     * Commands will persist between restarts of your bot, you only have to create a command once.
     *
     * <p>To specify a complete list of all commands you can use {@link #updateCommands()} instead.
     *
     * <p>You need the OAuth2 scope {@code "applications.commands"} in order to add commands to a guild.
     *
     * @param command The {@link CommandData} for the command
     * @return {@link RestAction} - Type: {@link Command}
     * <br>The RestAction used to create or update the command
     * @throws IllegalArgumentException If null is provided
     * @see Commands#slash(String, String) Commands.slash(...)
     * @see Commands#message(String) Commands.message(...)
     * @see Commands#user(String) Commands.user(...)
     */
    @NotNull
    @Override
    public RestAction<Command> upsertCommand(CommandData command) {
        return guild().upsertCommand(command);
    }

    /**
     * Configures the complete list of guild commands.
     * <br>This will replace the existing command list for this guild. You should only use this at most once on startup!
     *
     * <p>This operation is idempotent.
     * Commands will persist between restarts of your bot, you only have to create a command once.
     *
     * <p>You need the OAuth2 scope {@code "applications.commands"} in order to add commands to a guild.
     *
     * <p><b>Examples</b>
     *
     * <p>Set list to 2 commands:
     * <pre>{@code
     * guild.updateCommands()
     *   .addCommands(Commands.slash("ping", "Gives the current ping"))
     *   .addCommands(Commands.slash("ban", "Ban the target user")
     *     .addOption(OptionType.USER, "user", "The user to ban", true))
     *     .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
     *   .queue();
     * }</pre>
     *
     * <p>Delete all commands:
     * <pre>{@code
     * guild.updateCommands().queue();
     * }</pre>
     *
     * @return {@link CommandListUpdateAction}
     * @see JDA#updateCommands()
     */
    @NotNull
    @Override
    public CommandListUpdateAction updateCommands() {
        return guild().updateCommands();
    }

    /**
     * Edit an existing command by id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * @param id The id of the command to edit
     * @return {@link CommandEditAction} used to edit the command
     * @throws IllegalArgumentException If the provided id is not a valid snowflake
     */
    @NotNull
    @Override
    public CommandEditAction editCommandById(String id) {
        return guild().editCommandById(id);
    }

    /**
     * Delete the command for this id.
     *
     * <p>If there is no command with the provided ID,
     * this RestAction fails with {@link ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * @param commandId The id of the command that should be deleted
     * @return {@link RestAction}
     * @throws IllegalArgumentException If the provided id is not a valid snowflake
     */
    @NotNull
    @Override
    public RestAction<Void> deleteCommandById(String commandId) {
        return guild().deleteCommandById(commandId);
    }

    /**
     * Retrieves the {@link IntegrationPrivilege IntegrationPrivileges} for the target with the specified ID.
     * <br><b>The ID can either be of a Command or Application!</b>
     *
     * <p>Moderators of a guild can modify these privileges through the Integrations Menu
     *
     * <p>If there is no command or application with the provided ID,
     * this RestAction fails with {@link ErrorResponse#UNKNOWN_COMMAND ErrorResponse.UNKNOWN_COMMAND}
     *
     * @param targetId The id of the command (global or guild), or application
     * @return {@link RestAction} - Type: {@link List} of {@link IntegrationPrivilege}
     * @throws IllegalArgumentException If the id is not a valid snowflake
     */
    @NotNull
    @Override
    public RestAction<List<IntegrationPrivilege>> retrieveIntegrationPrivilegesById(String targetId) {
        return guild().retrieveIntegrationPrivilegesById(targetId);
    }

    /**
     * Retrieves the {@link IntegrationPrivilege IntegrationPrivileges} for the commands in this guild.
     * <br>The RestAction provides a {@link PrivilegeConfig} providing the privileges of this application and its commands.
     *
     * <p>Moderators of a guild can modify these privileges through the Integrations Menu
     *
     * @return {@link RestAction} - Type: {@link PrivilegeConfig}
     */
    @NotNull
    @Override
    public RestAction<PrivilegeConfig> retrieveCommandPrivileges() {
        return guild().retrieveCommandPrivileges();
    }

    /**
     * Retrieves the available regions for this Guild
     *
     * @param includeDeprecated Whether to include deprecated regions
     * @return {@link RestAction RestAction} - Type {@link EnumSet EnumSet}
     */
    @NotNull
    @Override
    public RestAction<EnumSet<Region>> retrieveRegions(boolean includeDeprecated) {
        return guild().retrieveRegions(includeDeprecated);
    }

    /**
     * Adds the user to this guild as a member.
     * <br>This requires an <b>OAuth2 Access Token</b> with the scope {@code guilds.join}.
     *
     * @param accessToken The access token
     * @param user        The {@link UserSnowflake} for the member to add.
     *                    This can be a member or user instance or {@link User#fromId(long)}.
     * @return {@link MemberAction MemberAction}
     * @throws IllegalArgumentException        If the access token is blank, empty, or null,
     *                                         or if the provided user reference is null or is already in this guild
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#CREATE_INSTANT_INVITE Permission.CREATE_INSTANT_INVITE}
     * @see <a href="https://discord.com/developers/docs/topics/oauth2" target="_blank">Discord OAuth2 Documentation</a>
     * @since 3.7.0
     */
    @NotNull
    @Override
    public MemberAction addMember(String accessToken, UserSnowflake user) {
        return guild().addMember(accessToken, user);
    }

    /**
     * Whether this guild has loaded members.
     * <br>This will always be false if the {@link GatewayIntent#GUILD_MEMBERS GUILD_MEMBERS} intent is disabled.
     *
     * @return True, if members are loaded.
     */
    @Override
    public boolean isLoaded() {
        return guild().isLoaded();
    }

    /**
     * Re-apply the {@link MemberCachePolicy MemberCachePolicy} of this session to all {@link Member Members} of this Guild.
     *
     * <h4>Example</h4>
     * <pre>{@code
     * // Check if the members of this guild have at least 50% bots (bot collection/farm)
     * public void checkBots(Guild guild) {
     *     // Keep in mind: This requires the GUILD_MEMBERS intent which is disabled in createDefault and createLight by default
     *     guild.retrieveMembers() // Load members CompletableFuture<Void> (async and eager)
     *          .thenApply((v) -> guild.getMemberCache()) // Turn into CompletableFuture<MemberCacheView>
     *          .thenAccept((members) -> {
     *              int total = members.size();
     *              // Casting to double to get a double as result of division, don't need to worry about precision with small counts like this
     *              double bots = (double) members.applyStream(stream ->
     *                  stream.map(Member::getUser)
     *                        .filter(User::isBot)
     *                        .count()); // Count bots
     *              if (bots / total > 0.5) // Check how many members are bots
     *                  System.out.println("More than 50% of members in this guild are bots");
     *          })
     *          .thenRun(guild::pruneMemberCache); // Then prune the cache
     * }
     * }</pre>
     *
     * @see #unloadMember(long)
     * @see JDA#unloadUser(long)
     */
    @Override
    public void pruneMemberCache() {

    }

    /**
     * Attempts to remove the user with the provided id from the member cache.
     * <br>If you attempt to remove the {@link JDA#getSelfUser() SelfUser} this will simply return {@code false}.
     *
     * <p>This should be used by an implementation of {@link MemberCachePolicy MemberCachePolicy}
     * as an upstream request to remove a member. For example a Least-Recently-Used (LRU) cache might use this to drop
     * old members if the cache capacity is reached. Or a timeout cache could use this to remove expired members.
     *
     * @param userId The target user id
     * @return True, if the cache was changed
     * @see #pruneMemberCache()
     * @see JDA#unloadUser(long)
     */
    @Override
    public boolean unloadMember(long userId) {
        return guild().unloadMember(userId);
    }

    /**
     * The expected member count for this guild.
     * <br>If this guild is not lazy loaded this should be identical to the size returned by {@link #getMemberCache()}.
     *
     * <p>When {@link GatewayIntent#GUILD_MEMBERS GatewayIntent.GUILD_MEMBERS} is disabled, this will not be updated.
     *
     * @return The expected member count for this guild
     */
    @Override
    public int getMemberCount() {
        return guild().getMemberCount();
    }

    /**
     * The human readable name of the {@link Guild Guild}.
     * <p>
     * This value can be modified using {@link GuildManager#setName(String)}.
     *
     * @return Never-null String containing the Guild's name.
     */
    @NotNull
    @Override
    public String getName() {
        return guild().getName();
    }

    /**
     * The Discord hash-id of the {@link Guild Guild} icon image.
     * If no icon has been set, this returns {@code null}.
     * <p>
     * The Guild icon can be modified using {@link GuildManager#setIcon(Icon)}.
     *
     * @return Possibly-null String containing the Guild's icon hash-id.
     */
    @Nullable
    @Override
    public String getIconId() {
        return guild().getIconId();
    }

    /**
     * The Features of the {@link Guild Guild}.
     * <p>
     * <a target="_blank" href="https://discord.com/developers/docs/resources/guild#guild-object-guild-features"><b>List of Features</b></a>
     *
     * @return Never-null, unmodifiable Set containing all of the Guild's features.
     */
    @NotNull
    @Override
    public Set<String> getFeatures() {
        return guild().getFeatures();
    }

    /**
     * The Discord hash-id of the splash image for this Guild. A Splash image is an image displayed when viewing a
     * Discord Guild Invite on the web or in client just before accepting or declining the invite.
     * If no splash has been set, this returns {@code null}.
     * <br>Splash images are VIP/Partner Guild only.
     * <p>
     * The Guild splash can be modified using {@link GuildManager#setSplash(Icon)}.
     *
     * @return Possibly-null String containing the Guild's splash hash-id
     */
    @Nullable
    @Override
    public String getSplashId() {
        return guild().getSplashId();
    }

    /**
     * The vanity url code for this Guild. The vanity url is the custom invite code of partnered / official / boosted Guilds.
     * <br>The returned String will be the code that can be provided to {@code discord.gg/{code}} to get the invite link.
     *
     * @return The vanity code or null
     * @see #getVanityUrl()
     * @since 4.0.0
     */
    @Nullable
    @Override
    public String getVanityCode() {
        return guild().getVanityCode();
    }

    /**
     * Retrieves the Vanity Invite meta data for this guild.
     * <br>This allows you to inspect how many times the vanity invite has been used.
     * You can use {@link #getVanityUrl()} if you only care about the invite.
     *
     * <p>This action requires the {@link Permission#MANAGE_SERVER MANAGE_SERVER} permission.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#INVITE_CODE_INVALID INVITE_CODE_INVALID}
     *     <br>If this guild does not have a vanity invite</li>
     *
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The vanity invite cannot be fetched due to a permission discrepancy</li>
     * </ul>
     *
     * @return {@link RestAction} - Type: {@link VanityInvite}
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#MANAGE_SERVER Permission.MANAGE_SERVER}
     * @since 4.2.1
     */
    @NotNull
    @Override
    public RestAction<VanityInvite> retrieveVanityInvite() {
        return guild().retrieveVanityInvite();
    }

    /**
     * The description for this guild.
     * <br>This is displayed in the server browser below the guild name for verified guilds.
     *
     * <p>The description can be modified using {@link GuildManager#setDescription(String)}.
     *
     * @return The description
     * @since 4.0.0
     */
    @Nullable
    @Override
    public String getDescription() {
        return guild().getDescription();
    }

    /**
     * The preferred locale for this guild.
     * <br>If the guild doesn't have the COMMUNITY feature, this returns the default.
     *
     * <br>Default: {@link Locale#US}
     *
     * @return The preferred {@link Locale} for this guild
     * @since 4.2.1
     */
    @NotNull
    @Override
    public Locale getLocale() {
        return guild().getLocale();
    }

    /**
     * The guild banner id.
     * <br>This is shown in guilds below the guild name.
     *
     * <p>The banner can be modified using {@link GuildManager#setBanner(Icon)}.
     *
     * @return The guild banner id or null
     * @see #getBannerUrl()
     * @since 4.0.0
     */
    @Nullable
    @Override
    public String getBannerId() {
        return guild().getBannerId();
    }

    /**
     * The boost tier for this guild.
     * <br>Each tier unlocks new perks for a guild that can be seen in the {@link #getFeatures() features}.
     *
     * @return The boost tier.
     * @since 4.0.0
     */
    @NotNull
    @Override
    public BoostTier getBoostTier() {
        return guild().getBoostTier();
    }

    /**
     * The amount of boosts this server currently has.
     *
     * @return The boost count
     * @since 4.0.0
     */
    @Override
    public int getBoostCount() {
        return guild().getBoostCount();
    }

    /**
     * Sorted list of {@link Member Members} that boost this guild.
     * <br>The list is sorted by {@link Member#getTimeBoosted()} ascending.
     * This means the first element will be the member who has been boosting for the longest time.
     *
     * <p>This will only check cached members!
     * <br>See {@link MemberCachePolicy MemberCachePolicy}
     *
     * @return Possibly-immutable list of members who boost this guild
     */
    @NotNull
    @Override
    public List<Member> getBoosters() {
        return guild().getBoosters();
    }

    /**
     * The maximum amount of members that can join this guild.
     *
     * @return The maximum amount of members
     * @see #retrieveMetaData()
     * @since 4.0.0
     */
    @Override
    public int getMaxMembers() {
        return guild().getMaxMembers();
    }

    /**
     * The maximum amount of connected members this guild can have at a time.
     * <br>This includes members that are invisible but still connected to discord.
     * If too many members are online the guild will become unavailable for others.
     *
     * @return The maximum amount of connected members this guild can have
     * @see #retrieveMetaData()
     * @since 4.0.0
     */
    @Override
    public int getMaxPresences() {
        return guild().getMaxPresences();
    }

    /**
     * Loads {@link MetaData} for this guild instance.
     *
     * @return {@link RestAction} - Type: {@link MetaData}
     * @since 4.2.0
     */
    @NotNull
    @Override
    public RestAction<MetaData> retrieveMetaData() {
        return guild().retrieveMetaData();
    }

    /**
     * Provides the {@link VoiceChannel VoiceChannel} that has been set as the channel
     * which {@link Member Members} will be moved to after they have been inactive in a
     * {@link VoiceChannel VoiceChannel} for longer than {@link #getAfkTimeout()}.
     * <br>If no channel has been set as the AFK channel, this returns {@code null}.
     * <p>
     * This value can be modified using {@link GuildManager#setAfkChannel(VoiceChannel)}.
     *
     * @return Possibly-null {@link VoiceChannel VoiceChannel} that is the AFK Channel.
     */
    @Nullable
    @Override
    public VoiceChannel getAfkChannel() {
        return guild().getAfkChannel();
    }

    /**
     * Provides the {@link TextChannel TextChannel} that has been set as the channel
     * which newly joined {@link Member Members} will be announced in.
     * <br>If no channel has been set as the system channel, this returns {@code null}.
     * <p>
     * This value can be modified using {@link GuildManager#setSystemChannel(TextChannel)}.
     *
     * @return Possibly-null {@link TextChannel TextChannel} that is the system Channel.
     */
    @Nullable
    @Override
    public TextChannel getSystemChannel() {
        return guild().getSystemChannel();
    }

    /**
     * Provides the {@link TextChannel TextChannel} that lists the rules of the guild.
     * <br>If this guild doesn't have the COMMUNITY {@link #getFeatures() feature}, this returns {@code null}.
     *
     * @return Possibly-null {@link TextChannel TextChannel} that is the rules channel
     * @see #getFeatures()
     */
    @Nullable
    @Override
    public TextChannel getRulesChannel() {
        return guild().getRulesChannel();
    }

    /**
     * Provides the {@link TextChannel TextChannel} that receives community updates.
     * <br>If this guild doesn't have the COMMUNITY {@link #getFeatures() feature}, this returns {@code null}.
     *
     * @return Possibly-null {@link TextChannel TextChannel} that is the community updates channel
     * @see #getFeatures()
     */
    @Nullable
    @Override
    public TextChannel getCommunityUpdatesChannel() {
        return guild().getCommunityUpdatesChannel();
    }

    /**
     * The {@link Member Member} object for the owner of this Guild.
     * <br>This is null when the owner is no longer in this guild or not yet loaded (lazy loading).
     * Sometimes owners of guilds delete their account or get banned by Discord.
     *
     * <p>If lazy-loading is used it is recommended to use {@link #retrieveOwner()} instead.
     *
     * <p>Ownership can be transferred using {@link Guild#transferOwnership(Member)}.
     *
     * <p>This only works when the member was added to cache. Lazy loading might load this later.
     * <br>See {@link MemberCachePolicy MemberCachePolicy}
     *
     * @return Possibly-null Member object for the Guild owner.
     * @see #getOwnerIdLong()
     * @see #retrieveOwner()
     */
    @Nullable
    @Override
    public Member getOwner() {
        return guild().getOwner();
    }

    /**
     * The ID for the current owner of this guild.
     * <br>This is useful for debugging purposes or as a shortcut.
     *
     * @return The ID for the current owner
     * @see #getOwner()
     */
    @Override
    public long getOwnerIdLong() {
        return guild().getOwnerIdLong();
    }

    /**
     * The {@link Timeout Timeout} set for this Guild representing the amount of time
     * that must pass for a Member to have had no activity in a {@link VoiceChannel VoiceChannel}
     * to be considered AFK. If {@link #getAfkChannel()} is not {@code null} (thus an AFK channel has been set) then Member
     * will be automatically moved to the AFK channel after they have been inactive for longer than the returned Timeout.
     * <br>Default is {@link Timeout#SECONDS_300 300 seconds (5 minutes)}.
     * <p>
     * This value can be modified using {@link GuildManager#setAfkTimeout(Timeout)}.
     *
     * @return The {@link Timeout Timeout} set for this Guild.
     */
    @NotNull
    @Override
    public Timeout getAfkTimeout() {
        return guild().getAfkTimeout();
    }

    /**
     * Used to determine if the provided {@link UserSnowflake} is a member of this Guild.
     *
     * <p>This will only check cached members! If the cache is not loaded (see {@link #isLoaded()}), this may return false despite the user being a member.
     * This is false when {@link #getMember(UserSnowflake)} returns {@code null}.
     *
     * @param user The user to check
     * @return True - if this user is present and cached in this guild
     */
    @Override
    public boolean isMember(UserSnowflake user) {
        return guild().isMember(user);
    }

    /**
     * Gets the {@link Member Member} object of the currently logged in account in this guild.
     * <br>This is basically {@link JDA#getSelfUser()} being provided to {@link #getMember(UserSnowflake)}.
     *
     * @return The Member object of the currently logged in account.
     */
    @NotNull
    @Override
    public Member getSelfMember() {
        return guild().getSelfMember();
    }

    /**
     * Returns the NSFW Level that this guild is classified with.
     * <br>For a short description of the different values, see {@link NSFWLevel NSFWLevel}.
     * <p>
     * This value can only be modified by Discord after reviewing the Guild.
     *
     * @return The NSFWLevel of this guild.
     */
    @NotNull
    @Override
    public NSFWLevel getNSFWLevel() {
        return guild().getNSFWLevel();
    }

    /**
     * Gets the Guild specific {@link Member Member} object for the provided
     * {@link UserSnowflake}.
     * <br>If the user is not in this guild or currently uncached, {@code null} is returned.
     *
     * <p>This will only check cached members!
     *
     * @param user The {@link UserSnowflake} for the member to get.
     *             This can be a member or user instance or {@link User#fromId(long)}.
     * @return Possibly-null {@link Member Member} for the related {@link User User}.
     * @throws IllegalArgumentException If the provided user is null
     * @see #retrieveMember(UserSnowflake)
     */
    @Nullable
    @Override
    public Member getMember(UserSnowflake user) {
        return guild().getMember(user);
    }

    /**
     * {@link MemberCacheView MemberCacheView} for all cached
     * {@link Member Members} of this Guild.
     *
     * <p>This will only provide cached members!
     * <br>See {@link MemberCachePolicy MemberCachePolicy}
     *
     * @return {@link MemberCacheView MemberCacheView}
     * @see #loadMembers()
     */
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

    /**
     * Populated list of {@link GuildChannel channels} for this guild.
     * This includes all types of channels, such as category/voice/text.
     *
     * <p>The returned list is ordered in the same fashion as it would be by the official discord client.
     * <ol>
     *     <li>TextChannel and NewsChannel without parent</li>
     *     <li>VoiceChannel without parent</li>
     *     <li>StageChannel without parent</li>
     *     <li>Categories
     *         <ol>
     *             <li>TextChannel and NewsChannel with category as parent</li>
     *             <li>VoiceChannel with category as parent</li>
     *             <li>StageChannel with category as parent</li>
     *         </ol>
     *     </li>
     * </ol>
     *
     * @param includeHidden Whether to include channels with denied {@link Permission#VIEW_CHANNEL View Channel Permission}
     * @return Immutable list of channels for this guild
     * @see #getChannels()
     */
    @NotNull
    @Override
    public List<GuildChannel> getChannels(boolean includeHidden) {
        return guild().getChannels(includeHidden);
    }

    /**
     * Sorted {@link SnowflakeCacheView SnowflakeCacheView} of
     * all cached {@link Role Roles} of this Guild.
     * <br>Roles are sorted according to their position.
     *
     * @return {@link SortedSnowflakeCacheView SortedSnowflakeCacheView}
     */
    @NotNull
    @Override
    public SortedSnowflakeCacheView<Role> getRoleCache() {
        return guild().getRoleCache();
    }

    /**
     * {@link SnowflakeCacheView SnowflakeCacheView} of
     * all cached {@link RichCustomEmoji Custom Emojis} of this Guild.
     * <br>This will be empty if {@link CacheFlag#EMOJI} is disabled.
     *
     * <p>This requires the {@link CacheFlag#EMOJI CacheFlag.EMOJI} to be enabled!
     *
     * @return {@link SnowflakeCacheView SnowflakeCacheView}
     * @see #retrieveEmojis()
     */
    @NotNull
    @Override
    public SnowflakeCacheView<RichCustomEmoji> getEmojiCache() {
        return guild().getEmojiCache();
    }

    /**
     * {@link SnowflakeCacheView SnowflakeCacheView} of
     * all cached {@link GuildSticker GuildStickers} of this Guild.
     * <br>This will be empty if {@link CacheFlag#STICKER} is disabled.
     *
     * <p>This requires the {@link CacheFlag#STICKER CacheFlag.STICKER} to be enabled!
     *
     * @return {@link SnowflakeCacheView SnowflakeCacheView}
     * @see #retrieveStickers()
     */
    @NotNull
    @Override
    public SnowflakeCacheView<GuildSticker> getStickerCache() {
        return guild().getStickerCache();
    }

    /**
     * Retrieves an immutable list of Custom Emojis together with their respective creators.
     *
     * <p>Note that {@link RichCustomEmoji#getOwner()} is only available if the currently
     * logged in account has {@link Permission#MANAGE_EMOJIS_AND_STICKERS Permission.MANAGE_EMOJIS_AND_STICKERS}.
     *
     * @return {@link RestAction RestAction} - Type: List of {@link RichCustomEmoji}
     */
    @NotNull
    @Override
    public RestAction<List<RichCustomEmoji>> retrieveEmojis() {
        return guild().retrieveEmojis();
    }

    /**
     * Retrieves a custom emoji together with its respective creator.
     * <br><b>This does not include unicode emoji.</b>
     *
     * <p>Note that {@link RichCustomEmoji#getOwner()} is only available if the currently
     * logged in account has {@link Permission#MANAGE_EMOJIS_AND_STICKERS Permission.MANAGE_EMOJIS_AND_STICKERS}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_EMOJI UNKNOWN_EMOJI}
     *     <br>If the provided id does not correspond to an emoji in this guild</li>
     * </ul>
     *
     * @param id The emoji id
     * @return {@link RestAction RestAction} - Type: {@link RichCustomEmoji}
     * @throws IllegalArgumentException If the provided id is not a valid snowflake
     */
    @NotNull
    @Override
    public RestAction<RichCustomEmoji> retrieveEmojiById(String id) {
        return guild().retrieveEmojiById(id);
    }

    /**
     * Retrieves all the stickers from this guild.
     * <br>This also includes {@link GuildSticker#isAvailable() unavailable} stickers.
     *
     * @return {@link RestAction} - Type: List of {@link GuildSticker}
     */
    @NotNull
    @Override
    public RestAction<List<GuildSticker>> retrieveStickers() {
        return guild().retrieveStickers();
    }

    /**
     * Attempts to retrieve a {@link GuildSticker} object for this guild based on the provided snowflake reference.
     *
     * <p>The returned {@link RestAction RestAction} can encounter the following Discord errors:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_STICKER UNKNOWN_STICKER}
     *     <br>Occurs when the provided id does not refer to a sticker known by Discord.</li>
     * </ul>
     *
     * @param sticker The reference of the requested {@link Sticker}.
     *                <br>Can be {@link RichSticker}, {@link StickerItem}, or {@link Sticker#fromId(long)}.
     * @return {@link RestAction RestAction} - Type: {@link GuildSticker}
     * <br>On request, gets the sticker with id matching provided id from Discord.
     * @throws IllegalArgumentException If null is provided
     */
    @NotNull
    @Override
    public RestAction<GuildSticker> retrieveSticker(StickerSnowflake sticker) {
        return guild().retrieveSticker(sticker);
    }

    /**
     * Modify a sticker using {@link GuildStickerManager}.
     * <br>You can update multiple fields at once, by calling the respective setters before executing the request.
     *
     * <p>The returned {@link RestAction RestAction} can encounter the following Discord errors:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_STICKER UNKNOWN_STICKER}
     *     <br>Occurs when the provided id does not refer to a sticker known by Discord.</li>
     * </ul>
     *
     * @param sticker
     * @return {@link GuildStickerManager}
     * @throws IllegalArgumentException        If null is provided
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#MANAGE_EMOJIS_AND_STICKERS MANAGE_EMOJIS_AND_STICKERS} in the guild.
     */
    @NotNull
    @Override
    public GuildStickerManager editSticker(StickerSnowflake sticker) {
        return guild().editSticker(sticker);
    }

    /**
     * Retrieves an immutable list of the currently banned {@link User Users}.
     * <br>If you wish to ban or unban a user, use either {@link #ban(UserSnowflake, int)} or
     * {@link #unban(UserSnowflake)}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The ban list cannot be fetched due to a permission discrepancy</li>
     * </ul>
     *
     * @return The {@link BanPaginationAction BanPaginationAction} of the guild's bans.
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#BAN_MEMBERS} permission.
     */
    @NotNull
    @Override
    public BanPaginationAction retrieveBanList() {
        return guild().retrieveBanList();
    }

    /**
     * Retrieves a {@link Ban Ban} of the provided {@link UserSnowflake}.
     * <br>If you wish to ban or unban a user, use either {@link #ban(UserSnowflake, int)} or {@link #unban(UserSnowflake)}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The ban list cannot be fetched due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_BAN UNKNOWN_BAN}
     *     <br>Either the ban was removed before finishing the task or it did not exist in the first place</li>
     * </ul>
     *
     * @param user The {@link UserSnowflake} for the banned user.
     *             This can be a user instance or {@link User#fromId(long)}.
     * @return {@link RestAction RestAction} - Type: {@link Ban Ban}
     * <br>An unmodifiable ban object for the user banned from this guild
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#BAN_MEMBERS} permission.
     */
    @NotNull
    @Override
    public RestAction<Ban> retrieveBan(UserSnowflake user) {
        return guild().retrieveBan(user);
    }

    /**
     * The method calculates the amount of Members that would be pruned if {@link #prune(int, Role...)} was executed.
     * Prunability is determined by a Member being offline for at least <i>days</i> days.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The prune count cannot be fetched due to a permission discrepancy</li>
     * </ul>
     *
     * @param days Minimum number of days since a member has been offline to get affected.
     * @return {@link RestAction RestAction} - Type: Integer
     * <br>The amount of Members that would be affected.
     * @throws InsufficientPermissionException If the account doesn't have {@link Permission#KICK_MEMBERS KICK_MEMBER} Permission.
     * @throws IllegalArgumentException        If the provided days are less than {@code 1} or more than {@code 30}
     */
    @NotNull
    @Override
    public RestAction<Integer> retrievePrunableMemberCount(int days) {
        return guild().retrievePrunableMemberCount(days);
    }

    /**
     * The @everyone {@link Role Role} of this {@link Guild Guild}.
     * <br>This role is special because its {@link Role#getPosition() position} is calculated as
     * {@code -1}. All other role positions are 0 or greater. This implies that the public role is <b>always</b> below
     * any custom roles created in this Guild. Additionally, all members of this guild are implied to have this role so
     * it is not included in the list returned by {@link Member#getRoles() Member.getRoles()}.
     * <br>The ID of this Role is the Guild's ID thus it is equivalent to using {@link #getRoleById(long) getRoleById(getIdLong())}.
     *
     * @return The @everyone {@link Role Role}
     */
    @NotNull
    @Override
    public Role getPublicRole() {
        return guild().getPublicRole();
    }

    /**
     * The default {@link BaseGuildMessageChannel BaseGuildMessageChannel} for a {@link Guild Guild}.
     * <br>This is the channel that the Discord client will default to opening when a Guild is opened for the first time when accepting an invite
     * that is not directed at a specific {@link BaseGuildMessageChannel BaseGuildMessageChannel}.
     *
     * <p>Note: This channel is the first channel in the guild (ordered by position) that the {@link #getPublicRole()}
     * has the {@link Permission#VIEW_CHANNEL Permission.VIEW_CHANNEL} in.
     *
     * @return The {@link BaseGuildMessageChannel BaseGuildMessageChannel} representing the default channel for this guild
     */
    @Nullable
    @Override
    public BaseGuildMessageChannel getDefaultChannel() {
        return guild().getDefaultChannel();
    }

    /**
     * Returns the {@link GuildManager GuildManager} for this Guild, used to modify
     * all properties and settings of the Guild.
     * <br>You modify multiple fields in one request by chaining setters before calling {@link RestAction#queue() RestAction.queue()}.
     *
     * <p>This is a lazy idempotent getter. The manager is retained after the first call.
     * This getter is not thread-safe and would require guards by the user.
     *
     * @return The Manager of this Guild
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#MANAGE_SERVER Permission.MANAGE_SERVER}
     */
    @NotNull
    @Override
    public GuildManager getManager() {
        return guild().getManager();
    }

    /**
     * Returns whether this Guild has its boost progress bar shown.
     *
     * @return True, if this Guild has its boost progress bar shown
     */
    @Override
    public boolean isBoostProgressBarEnabled() {
        return guild().isBoostProgressBarEnabled();
    }

    /**
     * A {@link PaginationAction PaginationAction} implementation
     * that allows to {@link Iterable iterate} over all {@link AuditLogEntry AuditLogEntries} of
     * this Guild.
     * <br>This iterates from the most recent action to the first logged one. (Limit 90 days into history by discord api)
     *
     * <h4>Examples</h4>
     * <pre>{@code
     * public void logBan(GuildBanEvent event) {
     *     Guild guild = event.getGuild();
     *     List<TextChannel> modLog = guild.getTextChannelsByName("mod-log", true);
     *     guild.retrieveAuditLogs()
     *          .type(ActionType.BAN) // filter by type
     *          .limit(1)
     *          .queue(list -> {
     *             if (list.isEmpty()) return;
     *             AuditLogEntry entry = list.get(0);
     *             String message = String.format("%#s banned %#s with reason %s",
     *                                            entry.getUser(), event.getUser(), entry.getReason());
     *             modLog.forEach(channel ->
     *               channel.sendMessage(message).queue()
     *             );
     *          });
     * }
     * }</pre>
     *
     * @return {@link AuditLogPaginationAction AuditLogPaginationAction}
     * @throws InsufficientPermissionException If the currently logged in account
     *                                         does not have the permission {@link Permission#VIEW_AUDIT_LOGS VIEW_AUDIT_LOGS}
     */
    @NotNull
    @Override
    public AuditLogPaginationAction retrieveAuditLogs() {
        return guild().retrieveAuditLogs();
    }

    /**
     * Used to leave a Guild. If the currently logged in account is the owner of this guild ({@link Guild#getOwner()})
     * then ownership of the Guild needs to be transferred to a different {@link Member Member}
     * before leaving using {@link #transferOwnership(Member)}.
     *
     * @return {@link RestAction RestAction} - Type: {@link Void}
     * @throws IllegalStateException Thrown if the currently logged in account is the Owner of this Guild.
     */
    @NotNull
    @Override
    public RestAction<Void> leave() {
        return guild().leave();
    }

    /**
     * Used to completely delete a Guild. This can only be done if the currently logged in account is the owner of the Guild.
     * <br>If the account has MFA enabled, use {@link #delete(String)} instead to provide the MFA code.
     *
     * @return {@link RestAction} - Type: {@link Void}
     * @throws PermissionException   Thrown if the currently logged in account is not the owner of this Guild.
     * @throws IllegalStateException If the currently logged in account has MFA enabled. ({@link SelfUser#isMfaEnabled()}).
     */
    @NotNull
    @Override
    public RestAction<Void> delete() {
        return guild().delete();
    }

    /**
     * Used to completely delete a guild. This can only be done if the currently logged in account is the owner of the Guild.
     * <br>This method is specifically used for when MFA is enabled on the logged in account {@link SelfUser#isMfaEnabled()}.
     * If MFA is not enabled, use {@link #delete()}.
     *
     * @param mfaCode The Multifactor Authentication code generated by an app like
     *                <a href="https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2" target="_blank">Google Authenticator</a>.
     *                <br><b>This is not the MFA token given to you by Discord.</b> The code is typically 6 characters long.
     * @return {@link RestAction} - Type: {@link Void}
     * @throws PermissionException      Thrown if the currently logged in account is not the owner of this Guild.
     * @throws IllegalArgumentException If the provided {@code mfaCode} is {@code null} or empty when {@link SelfUser#isMfaEnabled()} is true.
     */
    @NotNull
    @Override
    public RestAction<Void> delete(String mfaCode) {
        return guild().delete(mfaCode);
    }

    /**
     * The {@link AudioManager AudioManager} that represents the
     * audio connection for this Guild.
     * <br>If no AudioManager exists for this Guild, this will create a new one.
     * <br>This operation is synchronized on all audio managers for this JDA instance,
     * this means that calling getAudioManager() on any other guild while a thread is accessing this method may be locked.
     *
     * @return The AudioManager for this Guild.
     * @throws IllegalStateException If {@link GatewayIntent#GUILD_VOICE_STATES} is disabled
     * @see JDA#getAudioManagerCache() JDA.getAudioManagerCache()
     */
    @NotNull
    @Override
    public AudioManager getAudioManager() {
        return guild().getAudioManager();
    }

    /**
     * Once the currently logged in account is connected to a {@link StageChannel},
     * this will trigger a {@link GuildVoiceState#getRequestToSpeakTimestamp() Request-to-Speak} (aka raise your hand).
     *
     * <p>This will set an internal flag to automatically request to speak once the bot joins a stage channel.
     * <br>You can use {@link #cancelRequestToSpeak()} to move back to the audience or cancel your pending request.
     *
     * <p>If the self member has {@link Permission#VOICE_MUTE_OTHERS} this will immediately promote them to speaker.
     *
     * <p>Example:
     * <pre>{@code
     * stageChannel.createStageInstance("Talent Show").queue()
     * guild.requestToSpeak(); // Set request to speak flag
     * guild.getAudioManager().openAudioConnection(stageChannel); // join the channel
     * }</pre>
     *
     * @return {@link Task} representing the request to speak.
     * Calling {@link Task#get()} can result in deadlocks and should be avoided at all times.
     * @see #cancelRequestToSpeak()
     */
    @NotNull
    @Override
    public Task<Void> requestToSpeak() {
        return guild().requestToSpeak();
    }

    /**
     * Cancels the {@link #requestToSpeak() Request-to-Speak}.
     * <br>This can also be used to move back to the audience if you are currently a speaker.
     *
     * <p>If there is no request to speak or the member is not currently connected to a {@link StageChannel}, this does nothing.
     *
     * @return {@link Task} representing the request to speak cancellation.
     * Calling {@link Task#get()} can result in deadlocks and should be avoided at all times.
     * @see #requestToSpeak()
     */
    @NotNull
    @Override
    public Task<Void> cancelRequestToSpeak() {
        return guild().cancelRequestToSpeak();
    }

    /**
     * Returns the {@link JDA JDA} instance of this Guild
     *
     * @return the corresponding JDA instance
     */
    @NotNull
    @Override
    public JDA getJDA() {
        return guild().getJDA();
    }

    /**
     * Retrieves all {@link Invite Invites} for this guild.
     * <br>Requires {@link Permission#MANAGE_SERVER MANAGE_SERVER} in this guild.
     * Will throw an {@link InsufficientPermissionException InsufficientPermissionException} otherwise.
     *
     * <p>To get all invites for a {@link GuildChannel GuildChannel}
     * use {@link IInviteContainer#retrieveInvites() GuildChannel.retrieveInvites()}
     *
     * @return {@link RestAction RestAction} - Type: List{@literal <}{@link Invite Invite}{@literal >}
     * <br>The list of expanded Invite objects
     * @throws InsufficientPermissionException if the account does not have {@link Permission#MANAGE_SERVER MANAGE_SERVER} in this Guild.
     * @see IInviteContainer#retrieveInvites()
     */
    @NotNull
    @Override
    public RestAction<List<Invite>> retrieveInvites() {
        return guild().retrieveInvites();
    }

    /**
     * Retrieves all {@link Template Templates} for this guild.
     * <br>Requires {@link Permission#MANAGE_SERVER MANAGE_SERVER} in this guild.
     * Will throw an {@link InsufficientPermissionException InsufficientPermissionException} otherwise.
     *
     * @return {@link RestAction RestAction} - Type: List{@literal <}{@link Template Template}{@literal >}
     * <br>The list of Template objects
     * @throws InsufficientPermissionException if the account does not have {@link Permission#MANAGE_SERVER MANAGE_SERVER} in this Guild.
     */
    @NotNull
    @Override
    public RestAction<List<Template>> retrieveTemplates() {
        return guild().retrieveTemplates();
    }

    /**
     * Used to create a new {@link Template Template} for this Guild.
     * <br>Requires {@link Permission#MANAGE_SERVER MANAGE_SERVER} in this Guild.
     * Will throw an {@link InsufficientPermissionException InsufficientPermissionException} otherwise.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#ALREADY_HAS_TEMPLATE Guild already has a template}
     *     <br>The guild already has a template.</li>
     * </ul>
     *
     * @param name        The name of the template
     * @param description The description of the template
     * @return {@link RestAction RestAction} - Type: {@link Template Template}
     * <br>The created Template object
     * @throws InsufficientPermissionException if the account does not have {@link Permission#MANAGE_SERVER MANAGE_SERVER} in this Guild
     * @throws IllegalArgumentException        If the provided name is {@code null} or not between 1-100 characters long, or
     *                                         if the provided description is not between 0-120 characters long
     */
    @NotNull
    @Override
    public RestAction<Template> createTemplate(String name, String description) {
        return guild().createTemplate(name, description);
    }

    /**
     * Retrieves all {@link Webhook Webhooks} for this Guild.
     * <br>Requires {@link Permission#MANAGE_WEBHOOKS MANAGE_WEBHOOKS} in this Guild.
     *
     * <p>To get all webhooks for a specific {@link TextChannel TextChannel}, use
     * {@link TextChannel#retrieveWebhooks()}
     *
     * @return {@link RestAction RestAction} - Type: List{@literal <}{@link Webhook Webhook}{@literal >}
     * <br>A list of all Webhooks in this Guild.
     * @throws InsufficientPermissionException if the account does not have {@link Permission#MANAGE_WEBHOOKS MANAGE_WEBHOOKS} in this Guild.
     * @see TextChannel#retrieveWebhooks()
     */
    @NotNull
    @Override
    public RestAction<List<Webhook>> retrieveWebhooks() {
        return guild().retrieveWebhooks();
    }

    /**
     * A list containing the {@link GuildVoiceState GuildVoiceState} of every {@link Member Member}
     * in this {@link Guild Guild}.
     * <br>This will never return an empty list because if it were empty, that would imply that there are no
     * {@link Member Members} in this {@link Guild Guild}, which is
     * impossible.
     *
     * @return Never-empty immutable list containing all the {@link GuildVoiceState GuildVoiceStates} on this {@link Guild Guild}.
     */
    @NotNull
    @Override
    public List<GuildVoiceState> getVoiceStates() {
        return guild().getVoiceStates();
    }

    /**
     * Returns the verification-Level of this Guild. Verification level is one of the factors that determines if a Member
     * can send messages in a Guild.
     * <br>For a short description of the different values, see {@link VerificationLevel}.
     * <p>
     * This value can be modified using {@link GuildManager#setVerificationLevel(VerificationLevel)}.
     *
     * @return The Verification-Level of this Guild.
     */
    @NotNull
    @Override
    public VerificationLevel getVerificationLevel() {
        return guild().getVerificationLevel();
    }

    /**
     * Returns the default message Notification-Level of this Guild. Notification level determines when Members get notification
     * for messages. The value returned is the default level set for any new Members that join the Guild.
     * <br>For a short description of the different values, see {@link NotificationLevel NotificationLevel}.
     * <p>
     * This value can be modified using {@link GuildManager#setDefaultNotificationLevel(NotificationLevel)}.
     *
     * @return The default message Notification-Level of this Guild.
     */
    @NotNull
    @Override
    public NotificationLevel getDefaultNotificationLevel() {
        return guild().getDefaultNotificationLevel();
    }

    /**
     * Returns the level of multifactor authentication required to execute administrator restricted functions in this guild.
     * <br>For a short description of the different values, see {@link MFALevel MFALevel}.
     * <p>
     * This value can be modified using {@link GuildManager#setRequiredMFALevel(MFALevel)}.
     *
     * @return The MFA-Level required by this Guild.
     */
    @NotNull
    @Override
    public MFALevel getRequiredMFALevel() {
        return guild().getRequiredMFALevel();
    }

    /**
     * The level of content filtering enabled in this Guild.
     * <br>This decides which messages sent by which Members will be scanned for explicit content.
     *
     * @return {@link ExplicitContentLevel ExplicitContentLevel} for this Guild
     */
    @NotNull
    @Override
    public ExplicitContentLevel getExplicitContentLevel() {
        return guild().getExplicitContentLevel();
    }

    /**
     * Retrieves all members of this guild.
     * <br>This will use the configured {@link MemberCachePolicy MemberCachePolicy}
     * to decide which members to retain in cache.
     *
     * <p><b>This requires the privileged GatewayIntent.GUILD_MEMBERS to be enabled!</b>
     *
     * <p><b>You MUST NOT use blocking operations such as {@link Task#get()}!</b>
     * The response handling happens on the event thread by default.
     *
     * @param callback Consumer callback for each member
     * @return {@link Task} cancellable handle for this request
     * @throws IllegalArgumentException If the callback is null
     * @throws IllegalStateException    If the {@link GatewayIntent#GUILD_MEMBERS GatewayIntent.GUILD_MEMBERS} is not enabled
     */
    @NotNull
    @Override
    public Task<Void> loadMembers(Consumer<Member> callback) {
        return guild().loadMembers(callback);
    }

    /**
     * Load the member for the specified user.
     * <br>If the member is already loaded it will be retrieved from {@link #getMemberById(long)}
     * and immediately provided if the member information is consistent. The cache consistency directly
     * relies on the enabled {@link GatewayIntent GatewayIntents} as {@link GatewayIntent#GUILD_MEMBERS GatewayIntent.GUILD_MEMBERS}
     * is required to keep the cache updated with the latest information. You can pass {@code update = false} to always
     * return immediately if the member is cached regardless of cache consistency.
     *
     * <p>Possible {@link ErrorResponseException ErrorResponseExceptions} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER}
     *     <br>The specified user is not a member of this guild</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_USER}
     *     <br>The specified user does not exist</li>
     * </ul>
     *
     * @param id     The user id to load the member from
     * @param update Whether JDA should perform a request even if the member is already cached to update properties such as the name
     * @return {@link RestAction} - Type: {@link Member}
     * @see #pruneMemberCache()
     * @see #unloadMember(long)
     */
    @NotNull
    @Override
    public RestAction<Member> retrieveMemberById(long id, boolean update) {
        return guild().retrieveMemberById(id, update);
    }

    /**
     * Retrieves a list of members by their user id.
     * <br>If the id does not resolve to a member of this guild, then it will not appear in the resulting list.
     * It is possible that none of the IDs resolve to a member, in which case an empty list will be the result.
     *
     * <p>You can only load presences with the {@link GatewayIntent#GUILD_PRESENCES GUILD_PRESENCES} intent enabled.
     *
     * <p>The requests automatically timeout after {@code 10} seconds.
     * When the timeout occurs a {@link TimeoutException TimeoutException} will be used to complete exceptionally.
     *
     * <p><b>You MUST NOT use blocking operations such as {@link Task#get()}!</b>
     * The response handling happens on the event thread by default.
     *
     * @param includePresence Whether to load presences of the members (online status/activity)
     * @param ids             The ids of the members (max 100)
     * @return {@link Task} handle for the request
     * @throws IllegalArgumentException <ul>
     *                                              <li>If includePresence is {@code true} and the GUILD_PRESENCES intent is disabled</li>
     *                                              <li>If the input contains null</li>
     *                                              <li>If the input is more than 100 IDs</li>
     *                                          </ul>
     */
    @NotNull
    @Override
    public Task<List<Member>> retrieveMembersByIds(boolean includePresence, long... ids) {
        return guild().retrieveMembersByIds(includePresence, ids);
    }

    /**
     * Queries a list of members using a radix tree based on the provided name prefix.
     * <br>This will check both the username and the nickname of the members.
     * Additional filtering may be required. If no members with the specified prefix exist, the list will be empty.
     *
     * <p>The requests automatically timeout after {@code 10} seconds.
     * When the timeout occurs a {@link TimeoutException TimeoutException} will be used to complete exceptionally.
     *
     * <p><b>You MUST NOT use blocking operations such as {@link Task#get()}!</b>
     * The response handling happens on the event thread by default.
     *
     * @param prefix The case-insensitive name prefix
     * @param limit  The max amount of members to retrieve (1-100)
     * @return {@link Task} handle for the request
     * @throws IllegalArgumentException <ul>
     *                                              <li>If the provided prefix is null or empty.</li>
     *                                              <li>If the provided limit is not in the range of [1, 100]</li>
     *                                          </ul>
     * @see #getMembersByName(String, boolean)
     * @see #getMembersByNickname(String, boolean)
     * @see #getMembersByEffectiveName(String, boolean)
     */
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

    /**
     * This method will prune (kick) all members who were offline for at least <i>days</i> days.
     * <br>The RestAction returned from this method will return the amount of Members that were pruned.
     * <br>You can use {@link Guild#retrievePrunableMemberCount(int)} to determine how many Members would be pruned if you were to
     * call this method.
     *
     * <p>This might timeout when pruning many members with {@code wait=true}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The prune cannot finished due to a permission discrepancy</li>
     * </ul>
     *
     * @param days  Minimum number of days since a member has been offline to get affected.
     * @param wait  Whether to calculate the number of pruned members and wait for the response (timeout for too many pruned)
     * @param roles Optional roles to include in prune filter
     * @return {@link AuditableRestAction AuditableRestAction} - Type: Integer
     * <br>Provides the amount of Members that were pruned from the Guild, if wait is true.
     * @throws InsufficientPermissionException If the account doesn't have {@link Permission#KICK_MEMBERS KICK_MEMBER} Permission.
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If the provided days are not in the range from 1 to 30 (inclusive)</li>
     *                                                     <li>If null is provided</li>
     *                                                     <li>If any of the provided roles is not from this guild</li>
     *                                                 </ul>
     */
    @NotNull
    @Override
    public AuditableRestAction<Integer> prune(int days, boolean wait, Role... roles) {
        return guild().prune(days, wait, roles);
    }

    /**
     * Kicks the {@link UserSnowflake} from the {@link Guild Guild}.
     *
     * <p><b>Note:</b> {@link Guild#getMembers()} will still contain the {@link User User}
     * until Discord sends the {@link GuildMemberRemoveEvent GuildMemberRemoveEvent}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be kicked due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param user   The {@link UserSnowflake} for the user to kick.
     *               This can be a member or user instance or {@link User#fromId(long)}.
     * @param reason The reason for this action or {@code null} if there is no specified reason
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#KICK_MEMBERS} permission.
     * @throws HierarchyException              If the logged in account cannot kick the other member due to permission hierarchy position. (See {@link Member#canInteract(Member)})
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If the user cannot be kicked from this Guild or the provided {@code user} is null.</li>
     *                                                     <li>If the provided reason is longer than 512 characters</li>
     *                                                 </ul>
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> kick(UserSnowflake user, String reason) {
        return guild().kick(user, reason);
    }

    /**
     * Bans the user specified by the provided {@link UserSnowflake} and deletes messages sent by the user based on the amount of delDays.
     * <br>If you wish to ban a user without deleting any messages, provide delDays with a value of 0.
     *
     * <p>You can unban a user with {@link Guild#unban(UserSnowflake) Guild.unban(UserReference)}.
     *
     * <p><b>Note:</b> {@link Guild#getMembers()} will still contain the {@link User User's}
     * {@link Member Member} object (if the User was in the Guild)
     * until Discord sends the {@link GuildMemberRemoveEvent GuildMemberRemoveEvent}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be banned due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param user    The {@link UserSnowflake} for the user to ban.
     *                This can be a member or user instance or {@link User#fromId(long)}.
     * @param delDays The history of messages, in days, that will be deleted.
     * @param reason  The reason for this action or {@code null} if there is no specified reason
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#BAN_MEMBERS} permission.
     * @throws HierarchyException              If the logged in account cannot ban the other user due to permission hierarchy position.
     *                                         <br>See {@link Member#canInteract(Member)}
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If the provided amount of days (delDays) is less than 0.</li>
     *                                                     <li>If the provided amount of days (delDays) is bigger than 7.</li>
     *                                                     <li>If the provided reason is longer than 512 characters.</li>
     *                                                     <li>If the provided user is {@code null}</li>
     *                                                 </ul>
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> ban(UserSnowflake user, int delDays, String reason) {
        return guild().ban(user, delDays, reason);
    }

    /**
     * Unbans the specified {@link UserSnowflake} from this Guild.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be unbanned due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_USER UNKNOWN_USER}
     *     <br>The specified User is invalid</li>
     * </ul>
     *
     * @param user The {@link UserSnowflake} to unban.
     *             This can be a member or user instance or {@link User#fromId(long)}.
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#BAN_MEMBERS} permission.
     * @throws IllegalArgumentException        If the provided user is null
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> unban(UserSnowflake user) {
        return guild().unban(user);
    }

    /**
     * Puts the specified Member in time out in this {@link Guild Guild} until the specified date.
     * <br>While a Member is in time out, all permissions except {@link Permission#VIEW_CHANNEL VIEW_CHANNEL} and
     * {@link Permission#MESSAGE_HISTORY MESSAGE_HISTORY} are removed from them.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be put into time out due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param user     The {@link UserSnowflake} to timeout.
     *                 This can be a member or user instance or {@link User#fromId(long)}.
     * @param temporal The time the specified Member will be released from time out or null to remove the time out
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MODERATE_MEMBERS} permission.
     * @throws HierarchyException              If the logged in account cannot put a timeout on the other Member due to permission hierarchy position. (See {@link Member#canInteract(Member)})
     * @throws IllegalArgumentException        If any of the following are true
     *                                         <ul>
     *                                             <li>The provided {@code user} is null</li>
     *                                             <li>The provided {@code temporal} is in the past</li>
     *                                             <li>The provided {@code temporal} is more than {@value Member#MAX_TIME_OUT_LENGTH} days in the future</li>
     *                                         </ul>
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> timeoutUntil(UserSnowflake user, TemporalAccessor temporal) {
        return guild().timeoutUntil(user, temporal);
    }

    /**
     * Removes a time out from the specified Member in this {@link Guild Guild}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The time out cannot be removed due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param user The {@link UserSnowflake} to timeout.
     *             This can be a member or user instance or {@link User#fromId(long)}.
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#MODERATE_MEMBERS} permission.
     * @throws HierarchyException              If the logged in account cannot remove the timeout from the other Member due to permission hierarchy position. (See {@link Member#canInteract(Member)})
     */
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

    /**
     * Sets the Guild Muted state of the {@link Member Member} based on the provided
     * boolean.
     *
     * <p><b>Note:</b> The Member's {@link GuildVoiceState#isGuildMuted() GuildVoiceState.isGuildMuted()} value won't change
     * until JDA receives the {@link GuildVoiceGuildMuteEvent GuildVoiceGuildMuteEvent} event related to this change.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be muted due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     *
     *     <li>{@link ErrorResponse#USER_NOT_CONNECTED USER_NOT_CONNECTED}
     *     <br>The specified Member is not connected to a voice channel</li>
     * </ul>
     *
     * @param user The {@link UserSnowflake} who's {@link GuildVoiceState} to change.
     *             This can be a member or user instance or {@link User#fromId(long)}.
     * @param mute Whether this {@link Member Member} should be muted or unmuted.
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the logged in account does not have the {@link Permission#VOICE_DEAF_OTHERS} permission.
     * @throws IllegalArgumentException        If the provided user is null.
     * @throws IllegalStateException           If the provided user is not currently connected to a voice channel.
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> mute(UserSnowflake user, boolean mute) {
        return guild().mute(user, mute);
    }

    /**
     * Atomically assigns the provided {@link Role Role} to the specified {@link Member Member}.
     * <br><b>This can be used together with other role modification methods as it does not require an updated cache!</b>
     *
     * <p>If multiple roles should be added/removed (efficiently) in one request
     * you may use {@link #modifyMemberRoles(Member, Collection, Collection) modifyMemberRoles(Member, Collection, Collection)} or similar methods.
     *
     * <p>If the specified role is already present in the member's set of roles this does nothing.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The Members Roles could not be modified due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The target Member was removed from the Guild before finishing the task</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_ROLE UNKNOWN_ROLE}
     *     <br>If the specified Role does not exist</li>
     * </ul>
     *
     * @param user The {@link UserSnowflake} to change roles for.
     *             This can be a member or user instance or {@link User#fromId(long)}.
     * @param role The role which should be assigned atomically
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If the specified member or role are not from the current Guild</li>
     *                                                     <li>Either member or role are {@code null}</li>
     *                                                 </ul>
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#MANAGE_ROLES Permission.MANAGE_ROLES}
     * @throws HierarchyException              If the provided roles are higher in the Guild's hierarchy
     *                                         and thus cannot be modified by the currently logged in account
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> addRoleToMember(UserSnowflake user, Role role) {
        return guild().addRoleToMember(user, role);
    }

    /**
     * Atomically removes the provided {@link Role Role} from the specified {@link Member Member}.
     * <br><b>This can be used together with other role modification methods as it does not require an updated cache!</b>
     *
     * <p>If multiple roles should be added/removed (efficiently) in one request
     * you may use {@link #modifyMemberRoles(Member, Collection, Collection) modifyMemberRoles(Member, Collection, Collection)} or similar methods.
     *
     * <p>If the specified role is not present in the member's set of roles this does nothing.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The Members Roles could not be modified due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The target Member was removed from the Guild before finishing the task</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_ROLE UNKNOWN_ROLE}
     *     <br>If the specified Role does not exist</li>
     * </ul>
     *
     * @param user The {@link UserSnowflake} to change roles for.
     *             This can be a member or user instance or {@link User#fromId(long)}.
     * @param role The role which should be removed atomically
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If the specified member or role are not from the current Guild</li>
     *                                                     <li>Either member or role are {@code null}</li>
     *                                                 </ul>
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#MANAGE_ROLES Permission.MANAGE_ROLES}
     * @throws HierarchyException              If the provided roles are higher in the Guild's hierarchy
     *                                         and thus cannot be modified by the currently logged in account
     */
    @NotNull
    @Override
    public AuditableRestAction<Void> removeRoleFromMember(UserSnowflake user, Role role) {
        return guild().removeRoleFromMember(user, role);
    }

    /**
     * Modifies the {@link Role Roles} of the specified {@link Member Member}
     * by adding and removing a collection of roles.
     * <br>None of the provided roles may be the <u>Public Role</u> of the current Guild.
     * <br>If a role is both in {@code rolesToAdd} and {@code rolesToRemove} it will be removed.
     *
     * <h4>Example</h4>
     * <pre>{@code
     * public static void promote(Member member) {
     *     Guild guild = member.getGuild();
     *     List<Role> pleb = guild.getRolesByName("Pleb", true); // remove all roles named "pleb"
     *     List<Role> knight = guild.getRolesByName("Knight", true); // add all roles named "knight"
     *     // update roles in single request
     *     guild.modifyMemberRoles(member, knight, pleb).queue();
     * }
     * }</pre>
     *
     * <h4>Warning</h4>
     * <b>This may <u>not</u> be used together with any other role add/remove/modify methods for the same Member
     * within one event listener cycle! The changes made by this require cache updates which are triggered by
     * lifecycle events which are received later. This may only be called again once the specific Member has been updated
     * by a {@link GenericGuildMemberEvent GenericGuildMemberEvent} targeting the same Member.</b>
     *
     * <p>This is logically equivalent to:
     * <pre>{@code
     * Set<Role> roles = new HashSet<>(member.getRoles());
     * roles.addAll(rolesToAdd);
     * roles.removeAll(rolesToRemove);
     * RestAction<Void> action = guild.modifyMemberRoles(member, roles);
     * }</pre>
     *
     * <p>You can use {@link #addRoleToMember(UserSnowflake, Role)} and {@link #removeRoleFromMember(UserSnowflake, Role)} to make updates
     * independent of the cache.
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
     * @param member        The {@link Member Member} that should be modified
     * @param rolesToAdd    A {@link Collection Collection} of {@link Role Roles}
     *                      to add to the current Roles the specified {@link Member Member} already has, or null
     * @param rolesToRemove A {@link Collection Collection} of {@link Role Roles}
     *                      to remove from the current Roles the specified {@link Member Member} already has, or null
     * @return {@link AuditableRestAction AuditableRestAction}
     * @throws InsufficientPermissionException If the currently logged in account does not have {@link Permission#MANAGE_ROLES Permission.MANAGE_ROLES}
     * @throws HierarchyException              If the provided roles are higher in the Guild's hierarchy
     *                                         and thus cannot be modified by the currently logged in account
     * @throws IllegalArgumentException        <ul>
     *                                                     <li>If the target member is {@code null}</li>
     *                                                     <li>If any of the specified Roles is managed or is the {@code Public Role} of the Guild</li>
     *                                                 </ul>
     */
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