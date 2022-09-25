package net.ryanland.colossus.sys.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.attribute.IPermissionContainer;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.ryanland.colossus.sys.file.database.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public record ColossusMember(Member member) implements Member, ColossusDatabaseEntity {

    @Override
    public String getStockName() {
        return "members";
    }

    @Override
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(Map.of("_user_id", member.getId(), "_guild_id", member.getGuild().getId()));
    }

    
    @NotNull
    @Override
    public ColossusUser getUser() {
        return new ColossusUser(member().getUser());
    }

    
    @NotNull
    @Override
    public ColossusGuild getGuild() {
        return new ColossusGuild(member().getGuild());
    }

    
    @NotNull
    @Override
    public EnumSet<Permission> getPermissions() {
        return member().getPermissions();
    }

    
    @NotNull
    @Override
    public EnumSet<Permission> getPermissions(GuildChannel channel) {
        return member().getPermissions(channel);
    }

    
    @NotNull
    @Override
    public EnumSet<Permission> getPermissionsExplicit() {
        return member().getPermissionsExplicit();
    }

    
    @NotNull
    @Override
    public EnumSet<Permission> getPermissionsExplicit(GuildChannel channel) {
        return member().getPermissionsExplicit(channel);
    }

    
    @Override
    public boolean hasPermission(Permission... permissions) {
        return member().hasPermission(permissions);
    }

    
    @Override
    public boolean hasPermission(Collection<Permission> permissions) {
        return member().hasPermission(permissions);
    }

    
    @Override
    public boolean hasPermission(GuildChannel channel, Permission... permissions) {
        return member().hasPermission(channel, permissions);
    }

    
    @Override
    public boolean hasPermission(GuildChannel channel, Collection<Permission> permissions) {
        return member().hasPermission(channel, permissions);
    }

    @Override
    public boolean canSync(@NotNull IPermissionContainer targetChannel, @NotNull IPermissionContainer syncSource) {
        return member().canSync(targetChannel, syncSource);
    }

    
    @Override
    public boolean canSync(IPermissionContainer channel) {
        return member().canSync(channel);
    }

    
    @NotNull
    @Override
    public JDA getJDA() {
        return member().getJDA();
    }

    
    @NotNull
    @Override
    public OffsetDateTime getTimeJoined() {
        return member().getTimeJoined();
    }

    
    @Override
    public boolean hasTimeJoined() {
        return member().hasTimeJoined();
    }

    
    @Nullable
    @Override
    public OffsetDateTime getTimeBoosted() {
        return member().getTimeBoosted();
    }

    
    @Override
    public boolean isBoosting() {
        return member().isBoosting();
    }

    
    @Nullable
    @Override
    public OffsetDateTime getTimeOutEnd() {
        return member().getTimeOutEnd();
    }

    
    @Nullable
    @Override
    public GuildVoiceState getVoiceState() {
        return member().getVoiceState();
    }

    
    @NotNull
    @Override
    public List<Activity> getActivities() {
        return member().getActivities();
    }

    
    @NotNull
    @Override
    public OnlineStatus getOnlineStatus() {
        return member().getOnlineStatus();
    }

    
    @NotNull
    @Override
    public OnlineStatus getOnlineStatus(ClientType type) {
        return member().getOnlineStatus(type);
    }

    
    @NotNull
    @Override
    public EnumSet<ClientType> getActiveClients() {
        return member().getActiveClients();
    }

    
    @Nullable
    @Override
    public String getNickname() {
        return member().getNickname();
    }

    
    @NotNull
    @Override
    public String getEffectiveName() {
        return member().getEffectiveName();
    }

    
    @Nullable
    @Override
    public String getAvatarId() {
        return member().getAvatarId();
    }

    
    @NotNull
    @Override
    public List<Role> getRoles() {
        return member().getRoles();
    }

    
    @Nullable
    @Override
    public Color getColor() {
        return member().getColor();
    }

    
    @Override
    public int getColorRaw() {
        return member().getColorRaw();
    }

    
    @Override
    public boolean canInteract(Member member) {
        return member().canInteract(member);
    }

    
    @Override
    public boolean canInteract(Role role) {
        return member().canInteract(role);
    }

    
    @Override
    public boolean canInteract(RichCustomEmoji emoji) {
        return member().canInteract(emoji);
    }

    
    @Override
    public boolean isOwner() {
        return member().isOwner();
    }

    
    @Override
    public boolean isPending() {
        return member().isPending();
    }

    
    @Nullable
    @Override
    public DefaultGuildChannelUnion getDefaultChannel() {
        return member().getDefaultChannel();
    }

    
    @NotNull
    @Override
    public String getAsMention() {
        return member().getAsMention();
    }

    
    @Override
    public long getIdLong() {
        return member().getIdLong();
    }
}
