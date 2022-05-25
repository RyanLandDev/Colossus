package net.ryanland.colossus.command.permissions;

import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionBuilder {

    private final List<PermissionRequirement> requirements = new ArrayList<>();

    public PermissionBuilder add(Permission permission) {
        return addRequirements(new JDAPermissionRequirement(permission));
    }

    public PermissionBuilder addRequirements(PermissionRequirement... requirements) {
        this.requirements.addAll(List.of(requirements));
        return this;
    }

    public PermissionBuilder addHolder(PermissionHolder holder) {
        return addRequirements(new PermissionHolderRequirement(holder));
    }

    public PermissionHolder build() {
        return new PermissionHolder(requirements.toArray(new PermissionRequirement[0]));
    }

    public OptionalPermissionHolder buildOptional() {
        return new OptionalPermissionHolder(requirements.toArray(new PermissionRequirement[0]));
    }
}
