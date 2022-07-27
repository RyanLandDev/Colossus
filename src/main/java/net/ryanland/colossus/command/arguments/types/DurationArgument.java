package net.ryanland.colossus.command.arguments.types;

import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import net.ryanland.colossus.events.command.CommandEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationArgument extends ArgumentStringResolver<Duration> {

    private boolean strict = false;

    /**
     * Will change this argument to strictly check for the time format
     * by only allowing hours 0-23, minutes 0-59 and seconds 0-59.
     * <br>More specifically, uses the regex {@code ^(\d+d|)((0|0\d|\d|1\d|2[0-3])h|)(([0-5]\d|\d)m|)(([0-5]\d|\d)s|)$}
     * to check if the time is valid instead of {@code ^(\d+d|)(\d+h|)(\d+m|)(\d+s|)$}.
     * @return This argument
     */
    public Argument<Duration> useStrict() {
        strict = true;
        return this;
    }

    // will only allow hours 0-23, minutes 0-59, and seconds 0-59. allows all days
    private final String strictTimeRegex = "^(\\d+d|)((0|0\\d|\\d|1\\d|2[0-3])h|)(([0-5]\\d|\\d)m|)(([0-5]\\d|\\d)s|)$";

    // allows any amount of days, hours, minutes and seconds
    private final String timeRegex = "^(\\d+d|)(\\d+h|)(\\d+m|)(\\d+s|)$";

    @Override
    public Duration resolve(String arg, CommandEvent event) throws ArgumentException {
        // Check for argument validity
        if (!arg.matches(strict ? strictTimeRegex : timeRegex))
            throw new MalformedArgumentException("Invalid time. Example: 3d5h20m34s");

        // Get a list of all separate elements - ["3d", "5h", "20m", "34s"]
        List<String> matches = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\d+(d|h|m|s)").matcher(arg);
        while (matcher.find()) matches.add(matcher.group());

        // Repeat over all elements and form a Duration
        Duration duration = Duration.ofSeconds(0);
        for (String element : matches) {
            long amount = Long.parseLong(element.replaceAll("\\D", ""));
            String type = element.replaceAll("\\d", "");
            switch (type) {
                case "d" -> duration = duration.plusDays(amount);
                case "h" -> duration = duration.plusHours(amount);
                case "m" -> duration = duration.plusMinutes(amount);
                case "s" -> duration = duration.plusSeconds(amount);
            }
        }

        return duration;
    }
}
