package net.ryanland.colossus.command.permissions;

import net.dv8tion.jda.api.entities.Member;
import net.ryanland.colossus.command.Command;

/**
 * A permission requirement. This is used to check if a {@link Member} has permission to run a {@link Command}.
 */
public interface PermissionRequirement {

    /**
     * Check if the {@link Member} has permission.
     * @return {@code true} if the provided member has permission, {@code false} if not.
     */
    boolean check(Member member);

    /**
     * Returns the name of this requirement. Used for e.g. the help command.
     */
    String getName();

}
