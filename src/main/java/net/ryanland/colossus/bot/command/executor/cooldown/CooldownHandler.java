package net.ryanland.colossus.bot.command.executor.cooldown;

import net.dv8tion.jda.api.entities.User;
import net.ryanland.colossus.bot.command.executor.cooldown.manager.CooldownManager;
import net.ryanland.colossus.bot.command.executor.cooldown.manager.MemoryCooldownManager;
import net.ryanland.colossus.bot.command.impl.Command;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.sys.file.StorageType;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CooldownHandler {

    public static CooldownManager getCooldownManager(StorageType storage) {
        if (storage == StorageType.MEMORY) {
            return MemoryCooldownManager.getInstance();
        }
        if (storage == StorageType.LOCAL) {
            throw new RuntimeException("Unimplemented as there is no use for it currently.");
        }
        if (storage == StorageType.EXTERNAL) {
            throw new RuntimeException("Unimplemented");
        }

        throw new IllegalArgumentException();
    }

    public static CooldownManager getCooldownManager(CommandEvent event) {
        return getCooldownManager(event.getCommand().getCooldownStorageType());
    }

    public static boolean isCooldownActive(CommandEvent event) {
        cleanCooldowns(event);
        return getActiveCooldowns(event).stream()
            .anyMatch(cooldown -> cooldown.command().getName().equals(event.getCommand().getName()));
    }

    public static Cooldown getActiveCooldown(CommandEvent event) {
        if (!isCooldownActive(event)) return null;
        return getActiveCooldowns(event).stream()
            .filter(cooldown -> cooldown.command().getName().equals(event.getCommand().getName()))
            .collect(Collectors.toList())
            .get(0);
    }

    public static void newCooldown(CommandEvent event) {
        cleanCooldowns(event);
        getCooldownManager(event).put(event.getUser(), new Cooldown(event.getCommand(),
            new Date(System.currentTimeMillis() + event.getCommand().getCooldownInMs())));
    }

    public static List<Cooldown> getActiveCooldowns(CooldownManager manager, User user) {
        return manager.get(user);
    }

    public static List<Cooldown> getActiveCooldowns(CommandEvent event) {
        return getCooldownManager(event).get(event.getUser());
    }

    public static void cleanCooldowns(CommandEvent event) {
        cleanCooldowns(getCooldownManager(event.getCommand().getCooldownStorageType()), event.getUser());
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

    public static void removeCooldown(CommandEvent event) {
        removeCooldown(getCooldownManager(event.getCommand().getCooldownStorageType()),
            event.getUser(), event.getCommand());
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

    public static void purgeCooldowns(CommandEvent event) {
        getCooldownManager(event).purge(event.getUser());
    }
}
