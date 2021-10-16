package net.ryanland.colossus.command.annotations;

import net.ryanland.colossus.command.info.Category;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CommandBuilder {
    String name();
    String description();
    Category category();
    int cooldown() default 2;
}
