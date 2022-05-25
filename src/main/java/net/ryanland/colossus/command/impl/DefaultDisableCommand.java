package net.ryanland.colossus.command.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.CombinedCommand;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.BaseCommand;
import net.ryanland.colossus.command.annotations.CommandBuilder;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.CommandArgument;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import net.ryanland.colossus.command.permissions.BotOwnerRequirement;
import net.ryanland.colossus.command.permissions.PermissionBuilder;
import net.ryanland.colossus.command.permissions.PermissionHolder;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

@CommandBuilder(
        name = "disable",
        description = "Disables a command globally.",
        guildOnly = false
)
public final class DefaultDisableCommand extends DefaultCommand implements CombinedCommand {

    @Override
    public PermissionHolder getPermission() {
        return new PermissionBuilder()
            .addRequirements(new BotOwnerRequirement())
            .build();
    }

    @Override
    public ArgumentSet getArguments() {
        return new ArgumentSet().addArguments(
            new CommandArgument()
                .id("command")
                .description("Command to disable")
        );
    }

    @Override
    public void execute(CommandEvent event) throws CommandException {
        Command command = event.getArgument("command");
        DisabledCommandHandler.getInstance().disable(command);

        event.reply(
            new PresetBuilder(Colossus.getSuccessPresetType(),
                "Disabled the `" + command.getName() + "` command.")
        );
    }
}
