package net.ryanland.colossus.bot.command.executor.checks;

import net.ryanland.colossus.bot.command.executor.checks.impl.CooldownCheck;
import net.ryanland.colossus.bot.command.executor.checks.impl.DisabledCheck;
import net.ryanland.colossus.bot.command.executor.checks.impl.PermissionCheck;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.sys.message.builders.PresetBuilder;

public abstract class CommandCheck {

    @SuppressWarnings("all")
    private final static CommandCheck[] CHECKS = new CommandCheck[]{
        new DisabledCheck(),
        new PermissionCheck(),
        new CooldownCheck()
    };

    public static CommandCheck[] getChecks() {
        return CHECKS;
    }

    public abstract boolean check(CommandEvent event);

    public abstract PresetBuilder buildMessage(CommandEvent event);
}
