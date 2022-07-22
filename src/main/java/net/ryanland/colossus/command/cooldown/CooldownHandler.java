package net.ryanland.colossus.command.cooldown;

import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.ContextCommandEvent;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CooldownHandler {

    public static boolean isCooldownActive(CommandEvent event) {
        return isCooldownActive(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static boolean isCooldownActive(ContextCommandEvent<?> event) {
        return isCooldownActive(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static boolean isCooldownActive(CooldownManager manager, User user, BasicCommand command) {
        cleanCooldowns(manager, user);
        return getActiveCooldowns(manager, user).stream()
            .anyMatch(cooldown -> cooldown.command().getName().equals(command.getName()));
    }

    public static Cooldown getActiveCooldown(CommandEvent event) {
        return getActiveCooldown(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static Cooldown getActiveCooldown(ContextCommandEvent<?> event) {
        return getActiveCooldown(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static Cooldown getActiveCooldown(CooldownManager manager, User user, BasicCommand command) {
        if (!isCooldownActive(manager, user, command))
            return null;
        return getActiveCooldowns(manager, user).stream()
            .filter(cooldown -> cooldown.command().getName().equals(command.getName()))
            .toList().get(0);
    }

    public static void newCooldown(CommandEvent event) {
        newCooldown(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static void newCooldown(ContextCommandEvent<?> event) {
        newCooldown(event.getCommand().getCooldownManager(), event.getUser(), event.getCommand());
    }

    public static void newCooldown(CooldownManager manager, User user, BasicCommand command) {
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
            .toList();

        if (!activeCooldowns.equals(cooldowns)) {
            if (cooldowns.isEmpty()) {
                purgeCooldowns(manager, user);
            } else {
                manager.put(user, cooldowns);
            }
        }
    }

    public static void removeCooldown(CooldownManager manager, User user, BasicCommand command) {
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

    public static void purgeCooldowns(CooldownManager manager, User user) {
        manager.purge(user);
    }
}
