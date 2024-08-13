package dev.ryanland.colossus.sys.database.entities;

import dev.ryanland.colossus.events.repliable.RepliableEvent;
import dev.ryanland.colossus.sys.database.annotations.DefaultTable;
import dev.ryanland.colossus.sys.database.entities.MemberEntity.MemberEntityId;
import dev.ryanland.colossus.sys.snowflake.ColossusMember;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>You can create a subclass of this class to create a member table, following Hibernate's documentation.
 *
 * <p>This basically means that a unique reserved {@code _user_id} column and {@code _guild_id} column will be created. You may add more columns of your own to the table.
 *
 * <p>Annotate the class with {@link DefaultTable} to indicate that this is the default member table, which means that you can access
 * a member's data using the {@link RepliableEvent} class; e.g. {@code event.getMemberEntity()}, or the {@link ColossusMember} class; e.g. {@code member.getEntity()}.
 * <br><strong>Note:</strong> only one {@link MemberEntity} subclass can be annotated with {@link DefaultTable}.
 *
 * @see UserEntity
 */
@MappedSuperclass
@Getter
@IdClass(MemberEntityId.class)
public class MemberEntity extends ColossusEntity {

    @Id
    @Setter(AccessLevel.PUBLIC)
    @Column(name = "_user_id")
    private String userId;

    @Id
    @Setter(AccessLevel.PUBLIC)
    @Column(name = "_guild_id")
    private String guildId;

    @AllArgsConstructor
    public static class MemberEntityId {
        private String userId;
        private String guildId;
    }
}
