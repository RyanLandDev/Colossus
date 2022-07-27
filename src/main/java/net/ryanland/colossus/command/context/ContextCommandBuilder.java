package net.ryanland.colossus.command.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ContextCommandBuilder {

    String name();

    float cooldown() default 2;

    boolean guildOnly() default true;

    boolean canBeDisabled() default true;
}
