package net.ryanland.colossus.sys.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.ryanland.colossus.events.repliable.RepliableEvent;
import net.ryanland.colossus.sys.database.annotations.DefaultTable;
import net.ryanland.colossus.sys.snowflake.ColossusUser;

/**
 * <p>You can create a subclass of this class to create a user table, following Hibernate's documentation.
 *
 * <p>This basically means that a unique reserved {@code _user_id} column will be created. You may add more columns of your own to the table.
 *
 * <p>Annotate the class with {@link DefaultTable} to indicate that this is the default user table, which means that you can access
 * a user's data using the {@link RepliableEvent} class; e.g. {@code event.getUserEntity()}, or the {@link ColossusUser} class; e.g. {@code user.getEntity()}.
 * <br><strong>Note:</strong> only one {@link UserEntity} subclass can be annotated with {@link DefaultTable}.
 *
 * @see MemberEntity
 */
@MappedSuperclass
@Getter
public class UserEntity extends ColossusEntity {

    @Id
    @Setter(AccessLevel.PUBLIC)
    @Column(name = "_user_id")
    public String userId;
}
