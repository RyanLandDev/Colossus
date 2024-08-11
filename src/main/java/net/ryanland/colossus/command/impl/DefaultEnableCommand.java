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
    name = "enable",
    description = "Re-enables a globally disabled command.",
    guildOnly = false,
    canBeDisabled = false
)
public final class DefaultEnableCommand extends DefaultCommand implements CombinedCommand {

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
                        .stream().filter(BasicCommand::isDisabled).toList());
                }
            }
                .name("command")
                .description("Command to enable")
        );
    }

    @Override
    public void execute(CommandEvent event) throws CommandException {
        BasicCommand command = event.getArgument("command");
        DisabledCommandHandler.getInstance().enable(command);

        event.reply(
            new PresetBuilder(Colossus.getSuccessPresetType(),
                "Re-enabled the `" + command.getName() + "` command.")
        );
    }
}
