package net.ryanland.colossus.command.permission;

import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusEntity;
import net.ryanland.colossus.sys.entities.ColossusUser;

/**
 * A permission requirement. This is used to check if a {@link ColossusUser} has permission to run a {@link BasicCommand}.
 * @see UserPermissionRequirement
 * @see MemberPermissionRequirement
 * @see GuildPermissionRequirement
 */
public interface PermissionRequirement<T extends ColossusEntity> {

    /**
     * Returns {@link T} using the event
     */
    T fromCommandEvent(BasicCommandEvent event);

    /**
     * Check if this requirement passes with the provided event
     * @return {@code true} if the requirement passes, {@code false} if not.
     */
    default boolean check(BasicCommandEvent event) {
        return check(fromCommandEvent(event));
    }

    /**
     * Check if {@link T} has permission.
     * @return {@code true} if the provided entity has permission, {@code false} if not.
     */
    boolean check(T entity);

    /**
     * Returns the name of this requirement. Used for e.g. the help command.
     */
    String getName();

}
