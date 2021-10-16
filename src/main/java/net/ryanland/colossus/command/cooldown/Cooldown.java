package net.ryanland.colossus.command.cooldown;

import net.ryanland.colossus.command.Command;

import java.util.Date;

public record Cooldown(Command command, Date expires) {

}
