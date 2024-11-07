package dev.ryanland.colossus.events.annotations;

import dev.ryanland.colossus.ColossusBuilder;
import dev.ryanland.colossus.sys.interactions.select.BaseSelectMenu;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method is a static select menu listener. This method will be called automatically when a specific select menu is used.
 * <p>You should use the features provided by {@link PresetBuilder} instead if this is not a static select menu.
 * Using this annotation is recommended for select menus that are not part of a command. If you want to use a select menu within a command, use {@link BaseSelectMenu} instead.
 *
 * <p>Event consumers set using {@link PresetBuilder} will be ignored for select menus with the ID provided in this annotation.
 * 
 * <p>When using {@link ColossusBuilder#scanPackage(String)}, this annotation will automatically register the method as a listener.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SelectMenuListener {

    /**
     * The custom ID of the select menu that should be listened to.
     */
    String value();

    /**
     * Whether the select menu that should be listened to <i>exactly matches</i> or <i>starts with</i> the ID provided in {@link #value()}.
     *
     * <p>{@code false} = exactly matches<br>
     * {@code true} = starts with
     * <p><strong>Example: </strong> {@code manageuser-} will listen to {@code manageuser-1}, {@code manageuser-2}, etc. if this value is {@code true}.
     */
    boolean startsWith() default false;
}
