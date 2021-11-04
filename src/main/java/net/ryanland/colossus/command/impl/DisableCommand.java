package net.ryanland.colossus.command.impl;

import net.dv8tion.jda.api.Permission;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.*;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.types.CommandArgument;
import net.ryanland.colossus.command.executor.DisabledCommandHandler;
import net.ryanland.colossus.command.info.Category;
import net.ryanland.colossus.command.info.CommandInfo;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

public class DisableCommand extends Command implements CombinedCommand {

    @Override
    public CommandInfo getInfo() {
        return new CommandInfo()
            .name("disable")
            .description("Disables a command globally.")
            .category(Category.DEVELOPER)
            .permission(Permission.DEVELOPER)
            .flags(Flag.NO_DISABLE);
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
