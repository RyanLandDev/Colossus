package dev.ryanland.colossus.sys.snowflake;

import dev.ryanland.colossus.sys.database.HibernateManager;
import dev.ryanland.colossus.sys.database.entities.UserEntity;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.requests.restaction.CacheRestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public record ColossusUser(User user) implements User {

    public <R extends UserEntity> R getEntity() {
        return HibernateManager.getUser(getId());
    }

    @NotNull
    @Override
    public String getName() {
        return user().getName();
    }

    @Override
    public String getGlobalName() {
        return user().getGlobalName();
    }

    @NotNull
    @Override
    public String getDiscriminator() {
        return user().getDiscriminator();
    }
    
    @Nullable
    @Override
    public String getAvatarId() {
        return user().getAvatarId();
    }

    @NotNull
    @Override
    public String getDefaultAvatarId() {
        return user().getDefaultAvatarId();
    }

    @NotNull
    @Override
    public CacheRestAction<Profile> retrieveProfile() {
        return user().retrieveProfile();
    }

    @NotNull
    @Override
    public String getAsTag() {
        return user().getAsTag();
    }
    
    @Override
    public boolean hasPrivateChannel() {
        return user().hasPrivateChannel();
    }
    
    @NotNull
    @Override
    public CacheRestAction<PrivateChannel> openPrivateChannel() {
        return user().openPrivateChannel();
    }
    
    @NotNull
    @Override
    public List<Guild> getMutualGuilds() {
        return user().getMutualGuilds();
    }
    
    @Override
    public boolean isBot() {
        return user().isBot();
    }
    
    @Override
    public boolean isSystem() {
        return user().isSystem();
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return user().getJDA();
    }

    @NotNull
    @Override
    public EnumSet<UserFlag> getFlags() {
        return user().getFlags();
    }
    
    @Override
    public int getFlagsRaw() {
        return user().getFlagsRaw();
    }

    @NotNull
    @Override
    public String getAsMention() {
        return user().getAsMention();
    }

    @Override
    public long getIdLong() {
        return user().getIdLong();
    }
}
