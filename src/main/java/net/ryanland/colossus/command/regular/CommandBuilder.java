package net.ryanland.colossus.command.regular;

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

    boolean guildOnly() default true;

    boolean canBeDisabled() default true;
}
