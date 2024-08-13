package dev.ryanland.colossus.command.impl;

import dev.ryanland.colossus.command.BaseCommand;

/**
 * Default command provided by Colossus
 */
public abstract sealed class DefaultCommand extends BaseCommand
    permits DefaultHelpCommand {

}
