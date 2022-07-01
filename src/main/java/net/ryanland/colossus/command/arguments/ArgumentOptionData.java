package net.ryanland.colossus.command.arguments;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class ArgumentOptionData extends OptionData {

    public ArgumentOptionData(@NotNull OptionType type) {
        super(type, "name", "description");
    }
}
