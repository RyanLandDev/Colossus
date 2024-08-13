package dev.ryanland.colossus.sys.database.entities;

import dev.ryanland.colossus.events.repliable.RepliableEvent;
import dev.ryanland.colossus.sys.database.annotations.DefaultTable;
import dev.ryanland.colossus.sys.snowflake.ColossusGuild;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>You can create a subclass of this class to create a guild table, following Hibernate's documentation.
 *
 * <p>This basically means that a unique reserved {@code _guild_id} column will be created. You may add more columns of your own to the table.
 *
 * <p>Annotate the class with {@link DefaultTable} to indicate that this is the default guild table, which means that you can access
 * a guild's data using the {@link RepliableEvent} class; e.g. {@code event.getGuildEntity()}, or the {@link ColossusGuild} class; e.g. {@code guild.getEntity()}.
 * <br><strong>Note:</strong> only one {@link GuildEntity} subclass can be annotated with {@link DefaultTable}.
 */
@MappedSuperclass
@Getter
public class GuildEntity extends ColossusEntity {

    @Id
    @Setter(AccessLevel.PUBLIC)
    @Column(name = "_guild_id")
    private String guildId;
}
