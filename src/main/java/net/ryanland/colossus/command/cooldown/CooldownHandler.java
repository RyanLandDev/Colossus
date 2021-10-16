package net.ryanland.colossus.command.cooldown;

import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.events.ContentCommandEvent;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CooldownHandler {

    public static boolean isCooldownActive(ContentCommandEvent event) {
        return isCooldownActive(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static boolean isCooldownActive(CooldownManager manager, User user, Command command) {
        cleanCooldowns(manager, user);
        return getActiveCooldowns(manager, user).stream()
            .anyMatch(cooldown -> cooldown.command().getName().equals(command.getName()));
    }

    public static Cooldown getActiveCooldown(ContentCommandEvent event) {
        return getActiveCooldown(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static Cooldown getActiveCooldown(CooldownManager manager, User user, Command command) {
        if (!isCooldownActive(manager, user, command))
            return null;
        return getActiveCooldowns(manager, user).stream()
            .filter(cooldown -> cooldown.command().getName().equals(command.getName()))
            .collect(Collectors.toList())
            .get(0);
    }

    public static void newCooldown(ContentCommandEvent event) {
        newCooldown(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static void newCooldown(CooldownManager manager, User user, Command command) {
        cleanCooldowns(manager, user);
        manager.put(user, new Cooldown(command,
            new Date(System.currentTimeMillis() + command.getCooldownInMs())));
    }

    public static List<Cooldown> getActiveCooldowns(CooldownManager manager, User user) {
        return manager.get(user);
    }

    public static void cleanCooldowns(CooldownManager manager, User user) {
        Date date = new Date();
        List<Cooldown> activeCooldowns = getActiveCooldowns(manager, user);
        List<Cooldown> cooldowns = activeCooldowns.stream()
            .filter(cooldown -> date.before(cooldown.expires()))
            .collect(Collectors.toList());

        if (!activeCooldowns.equals(cooldowns)) {
            if (cooldowns.isEmpty()) {
                purgeCooldowns(manager, user);
            } else {
                manager.put(user, cooldowns);
            }
        }
    }

    public static void removeCooldown(CooldownManager manager, User user, Command command) {
        List<Cooldown> activeCooldowns = getActiveCooldowns(manager, user);
        List<Cooldown> cooldowns = activeCooldowns.stream()
            .filter(cooldown -> !cooldown.command().getName().equals(command.getName()))
            .collect(Collectors.toList());

        if (!activeCooldowns.equals(cooldowns)) {
            if (cooldowns.isEmpty()) {
                purgeCooldowns(manager, user);
            } else {
                manager.put(user, cooldowns);
            }
        }
    }

    public static void purgeCooldowns(CooldownManager manager, User user) {
        manager.purge(user);
    }
}
