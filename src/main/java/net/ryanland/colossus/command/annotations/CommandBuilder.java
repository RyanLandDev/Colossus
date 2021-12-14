package net.ryanland.colossus.command.annotations;

import net.ryanland.colossus.command.SubCommand;
import net.ryanland.colossus.command.info.SubCommandGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CommandBuilder {
    String name();
    String description();
    int cooldown() default 2;
    boolean guildOnly();
    Class<? extends SubCommand>[] subcommands() default {};
    Class<? extends SubCommandGroup>[] subcommandGroups() default {};
}
