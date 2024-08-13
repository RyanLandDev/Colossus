package dev.ryanland.colossus.command.cooldown;

import dev.ryanland.colossus.events.command.BasicCommandEvent;
import dev.ryanland.colossus.command.BasicCommand;
import dev.ryanland.colossus.sys.snowflake.ColossusUser;

import java.util.Date;
import java.util.List;

public class CooldownHandler {

    public static boolean isCooldownActive(BasicCommandEvent event) {
        return isCooldownActive(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static boolean isCooldownActive(CooldownManager manager, ColossusUser user, BasicCommand command) {
        cleanCooldowns(manager, user);
        return getActiveCooldowns(manager, user).stream()
            .anyMatch(cooldown -> cooldown.command().getName().equals(command.getName()));
    }

    public static Cooldown getActiveCooldown(BasicCommandEvent event) {
        return getActiveCooldown(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static Cooldown getActiveCooldown(CooldownManager manager, ColossusUser user, BasicCommand command) {
        if (!isCooldownActive(manager, user, command))
            return null;
        return getActiveCooldowns(manager, user).stream()
            .filter(cooldown -> cooldown.command().getName().equals(command.getName()))
            .toList().get(0);
    }

    public static void newCooldown(BasicCommandEvent event) {
        newCooldown(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static void newCooldown(CooldownManager manager, ColossusUser user, BasicCommand command) {
        cleanCooldowns(manager, user);
        manager.put(user, new Cooldown(command,
            new Date(System.currentTimeMillis() + command.getCooldownInMs())));
    }

    public static List<Cooldown> getActiveCooldowns(CooldownManager manager, ColossusUser user) {
        return manager.get(user);
    }

    public static void cleanCooldowns(CooldownManager manager, ColossusUser user) {
        Date date = new Date();
        List<Cooldown> activeCooldowns = getActiveCooldowns(manager, user);
        List<Cooldown> cooldowns = activeCooldowns.stream()
            .filter(cooldown -> date.before(cooldown.expires()))
            .toList();

        if (!activeCooldowns.equals(cooldowns)) {
            if (cooldowns.isEmpty()) {
                purgeCooldowns(manager, user);
            } else {
                manager.put(user, cooldowns);
            }
        }
    }

    public static void removeCooldown(CooldownManager manager, ColossusUser user, BasicCommand command) {
        List<Cooldown> activeCooldowns = getActiveCooldowns(manager, user);
        List<Cooldown> cooldowns = activeCooldowns.stream()
            .filter(cooldown -> !cooldown.command().getName().equals(command.getName()))
            .toList();

        if (!activeCooldowns.equals(cooldowns)) {
            if (cooldowns.isEmpty()) {
                purgeCooldowns(manager, user);
            } else {
                manager.put(user, cooldowns);
            }
        }
    }

    public static void purgeCooldowns(CooldownManager manager, ColossusUser user) {
        manager.purge(user);
    }
}
