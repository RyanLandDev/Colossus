package dev.ryanland.colossus.command.permission;

import dev.ryanland.colossus.command.BasicCommand;
import dev.ryanland.colossus.events.command.BasicCommandEvent;
import dev.ryanland.colossus.sys.snowflake.ColossusUser;

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
