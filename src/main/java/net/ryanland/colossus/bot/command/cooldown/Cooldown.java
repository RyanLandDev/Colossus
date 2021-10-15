package net.ryanland.colossus.bot.command.cooldown;

import net.ryanland.colossus.bot.command.Command;

import java.util.Date;

public record Cooldown(Command command, Date expires) {

}
