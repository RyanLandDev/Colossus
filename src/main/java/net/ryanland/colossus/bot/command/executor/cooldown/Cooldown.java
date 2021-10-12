package net.ryanland.colossus.bot.command.executor.cooldown;

import net.ryanland.colossus.bot.command.impl.Command;

import java.util.Date;

public record Cooldown(Command command, Date expires) {

}
