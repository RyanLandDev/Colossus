package dev.ryanland.colossus.events.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method is a static button listener. This method will be called automatically when a specific button is clicked.
 * <p>You should use the features provided by {@link dev.ryanland.colossus.sys.presetbuilder.PresetBuilder} instead if this is not a static button.
 * Using this annotation is recommended for buttons that are not part of a command. If you want to use a button within a command, use {@link dev.ryanland.colossus.sys.interactions.button.BaseButton} instead.
 *
 * <p>Event consumers set using {@link dev.ryanland.colossus.sys.presetbuilder.PresetBuilder} will be ignored for buttons with the ID provided in this annotation.
 * 
 * <p>When using {@link dev.ryanland.colossus.ColossusBuilder#scanPackage(String)}, this annotation will automatically register the method as a listener.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ButtonListener {

    /**
     * The custom ID of the button that should be listened to.
     */
    String value();

    /**
     * What the button ID of the button that should be listened to should start with.
     * <p><strong>Example: </strong> {@code closeticket-} will listen to {@code closeticket-1}, {@code closeticket-2}, etc.
     * <p>The {@link #value()} will be ignored if this is set (and not empty).
     */
    String startsWith() default "";
}
