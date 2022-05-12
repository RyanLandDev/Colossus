package net.ryanland.colossus.command.impl;

import net.ryanland.colossus.command.BaseCommand;

/**
 * Default command provided by Colossus
 */
public abstract sealed class DefaultCommand extends BaseCommand
    permits DefaultHelpCommand, DefaultDisableCommand, DefaultEnableCommand {

}
