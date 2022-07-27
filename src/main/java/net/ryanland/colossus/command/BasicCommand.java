package net.ryanland.colossus.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.ColossusBuilder;
import net.ryanland.colossus.command.cooldown.CooldownManager;
import net.ryanland.colossus.command.permission.PermissionHolder;

public abstract sealed class BasicCommand permits Command, ContextCommand {

    public abstract String getName();

    public final String getUppercaseName() {
        return getName().substring(0, 1).toUpperCase() + getName().substring(1);
    }

    public abstract CommandType getCommandType();

    public abstract boolean hasCooldown();

    public abstract int getCooldown();

    public final int getCooldownInMs() {
        return getCooldown() * 1000;
    }

    public abstract CooldownManager getCooldownManager();

    public abstract boolean isGuildOnly();

    public abstract boolean canBeDisabled();

    public abstract boolean isDisabled();

    /**
     * Gets the {@link LocalizationFunction} used for this command.<br>
     * By default, this is the localization function defined in your {@link ColossusBuilder}
     * using {@link ColossusBuilder#setLocalizationFunction(LocalizationFunction)}.<br>
     * You could override this method to use a unique localization function instead.
     *
     * @see LocalizationFunction
     * @see ResourceBundleLocalizationFunction
     * @see ColossusBuilder#setLocalizationFunction(LocalizationFunction)
     */
    public LocalizationFunction getLocalizationFunction() {
        return Colossus.getLocalizationFunction();
    }

    public abstract PermissionHolder getPermission();

    /**
     * Gets the {@link Permission Permissions} that a user must have in a specific channel to be able to use this command.
     * <br>By default, everyone can use this command ({@link DefaultMemberPermissions#ENABLED})
     * unless the command is disabled ({@link #isDisabled()}).
     * Additionally, a command can be disabled for everyone but admins via {@link DefaultMemberPermissions#DISABLED}.
     *
     * <p>You can change this by overriding this method.
     * <p>These configurations can be overwritten by moderators in each guild.
     * See {@link net.dv8tion.jda.api.interactions.commands.Command#retrievePrivileges(Guild)} to get moderator defined overrides.
     *
     * @return {@link DefaultMemberPermissions} representing the default permissions of this command.
     * @see DefaultMemberPermissions#ENABLED
     * @see DefaultMemberPermissions#DISABLED
     */
    public DefaultMemberPermissions getDefaultPermissions() {
        return isDisabled() ? DefaultMemberPermissions.DISABLED : DefaultMemberPermissions.ENABLED;
    }
}
