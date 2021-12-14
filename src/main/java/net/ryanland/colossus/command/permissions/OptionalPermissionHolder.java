package net.ryanland.colossus.command.permissions;

import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Modified {@link PermissionHolder} with one difference:<br>
 * Instead of only passing when ALL requirements are met, this holder will pass when ANY requirement is met.
 */
public class OptionalPermissionHolder extends PermissionHolder {

    public OptionalPermissionHolder(PermissionRequirement... requirements) {
        super(requirements);
    }

    @Override
    public boolean check(Member member) {
        for (PermissionRequirement requirement : requirements) {
            if (requirement.check(member))
                return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return Arrays.stream(requirements)
            .map(PermissionRequirement::getName)
            .collect(Collectors.joining(" or "));
    }
}
