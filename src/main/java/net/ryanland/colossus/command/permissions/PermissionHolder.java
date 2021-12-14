package net.ryanland.colossus.command.permissions;

import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Holds a list of {@link PermissionRequirement}s.<br>
 * To easily create a holder, refer to the {@link PermissionBuilder} class.
 * @see PermissionBuilder
 * @see PermissionRequirement
 */
public class PermissionHolder {

    protected final PermissionRequirement[] requirements;

    public PermissionHolder(PermissionRequirement... requirements) {
        this.requirements = requirements;
    }

    public PermissionRequirement[] getRequirements() {
        return requirements;
    }

    /**
     * Check if the provided member passes all requirements in this holder.
     */
    public boolean check(Member member) {
        for (PermissionRequirement requirement : requirements) {
            if (!requirement.check(member))
                return false;
        }
        return true;
    }

    /**
     * Whether there are no requirements present in this holder.
     */
    public boolean isEmpty() {
        return requirements.length == 0;
    }

    /**
     * Generates a combined String of all names of all requirements in this holder.
     */
    public String getName() {
        return Arrays.stream(requirements)
            .map(PermissionRequirement::getName)
            .collect(Collectors.joining(" & "));
    }
}
