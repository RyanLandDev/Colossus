package net.ryanland.colossus.command.permission;

import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.events.command.BasicCommandEvent;
import net.ryanland.colossus.sys.snowflake.ColossusUser;

/**
 * A permission requirement. This is used to check if a {@link ColossusUser} has permission to run a {@link BasicCommand}.
 */
public interface PermissionRequirement {

    /**
     * Check if this requirement passes with the provided event
     * @return {@code true} if the requirement passes, {@code false} if not.
     */
    boolean check(BasicCommandEvent event);

    /**
     * Returns the name of this requirement. Used for e.g. the help command.
     */
    String getName();

}
