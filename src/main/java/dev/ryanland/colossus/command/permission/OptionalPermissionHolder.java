package dev.ryanland.colossus.command.permission;

import dev.ryanland.colossus.events.command.BasicCommandEvent;

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
    public boolean check(BasicCommandEvent event) {
        for (PermissionRequirement requirement : requirements) {
            if (requirement.check(event)) return true;
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
