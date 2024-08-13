package dev.ryanland.colossus.command.cooldown;

import dev.ryanland.colossus.command.BasicCommand;

import java.util.Date;

public record Cooldown(BasicCommand command, Date expires) {

}
