package net.ryanland.colossus.command.impl;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.command.BasicCommandArgument;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import net.ryanland.colossus.command.permission.PermissionBuilder;
import net.ryanland.colossus.command.permission.PermissionHolder;
import net.ryanland.colossus.command.permission.impl.BotOwnerRequirement;
import net.ryanland.colossus.command.regular.CombinedCommand;
import net.ryanland.colossus.command.regular.CommandBuilder;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;

@CommandBuilder(
    name = "disable",
    description = "Disables a command globally.",
    guildOnly = false,
    canBeDisabled = false
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
            new BasicCommandArgument() {
                @Override
                public ArgumentOptionData getArgumentOptionData() {
                    return BasicCommandArgument.getAutocompleteChoiceData(CommandHandler.getAllCommands()
                        .stream().filter(command -> !command.isDisabled() && command.canBeDisabled()).toList());
                }
            }
                .name("command")
                .description("Command to disable")
        );
    }

    @Override
    public void execute(CommandEvent event) throws CommandException {
        BasicCommand command = event.getArgument("command");
        DisabledCommandHandler.getInstance().disable(command);

        event.reply(
            new PresetBuilder(Colossus.getSuccessPresetType(),
                "Disabled the `" + command.getName() + "` command.")
        );
    }
}
