package net.ryanland.colossus.bot.command;

import net.ryanland.colossus.ColossusBuilder;

/**
 * The way commands should be executed by the end user
 * @see ColossusBuilder
 */
public enum CommandExecutionType {

    /**
     * The classic command system. Messages starting with a @mention/configured prefix will be parsed for commands.
     * <br>Example: "!ping" -> "Ping: 50ms"
     * <br>Note: If your bot is verified, you should use {@link #SLASH} instead, because you will need the privileged
     * <i>Message Content</i> intent.
     */
    CONTENT,

    /**
     * Slash commands. When the user types "/", a menu will appear with all available slash commands.
     * <br>There are several advantages to using slash commands, such as providing built-in help instructions for your users.
     * <br>You can learn more about slash commands <a href="https://blog.discord.com/slash-commands-are-here-8db0a385d9e6">here</a>.
     */
    SLASH
}
