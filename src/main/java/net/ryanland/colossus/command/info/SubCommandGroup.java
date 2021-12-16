package net.ryanland.colossus.command.info;

import net.ryanland.colossus.command.SubCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.LOCAL_VARIABLE})
public @interface SubCommandGroup {

    String name();

    String description();

    Class<? extends SubCommand>[] subcommands();
}
