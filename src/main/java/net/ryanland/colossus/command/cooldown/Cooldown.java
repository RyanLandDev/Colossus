package net.ryanland.colossus.command.cooldown;

import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.Command;

import java.util.Date;

public record Cooldown(BasicCommand command, Date expires) {

}
