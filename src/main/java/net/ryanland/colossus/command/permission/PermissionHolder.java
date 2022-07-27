package net.ryanland.colossus.command.permission;

import net.ryanland.colossus.events.command.BasicCommandEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Holds a list of {@link PermissionRequirement PermissionRequirements}.<br>
 * To easily create a holder, refer to the {@link PermissionBuilder} class.
 * @see PermissionBuilder
 * @see PermissionRequirement
 */
public class PermissionHolder {

    protected final PermissionRequirement<?>[] requirements;

    public PermissionHolder(PermissionRequirement<?>... requirements) {
        this.requirements = requirements;
    }

    public PermissionRequirement<?>[] getRequirements() {
        return requirements;
    }

    /**
     * Check if the provided event passes all requirements in this holder
     */
    public boolean check(BasicCommandEvent event) {
        for (PermissionRequirement<?> requirement : requirements) {
            if (!requirement.check(event)) return false;
        }
        return true;
    }

    /**
     * Whether there are no requirements present in this holder
     */
    public boolean isEmpty() {
        return requirements.length == 0;
    }

    /**
     * Generates a combined String of all names of all requirements in this holder
     */
    public String getName() {
        return Arrays.stream(requirements)
            .map(PermissionRequirement::getName)
            .collect(Collectors.joining(" & "));
    }
}
