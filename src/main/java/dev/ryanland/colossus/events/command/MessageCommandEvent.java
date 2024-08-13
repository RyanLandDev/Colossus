package dev.ryanland.colossus.events.command;

import dev.ryanland.colossus.command.Command;
import dev.ryanland.colossus.command.arguments.ParsedArgumentMap;
import dev.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import dev.ryanland.colossus.command.regular.SubCommandHolder;
import dev.ryanland.colossus.events.repliable.ModalSubmitEvent;
import dev.ryanland.colossus.sys.config.Config;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import dev.ryanland.colossus.sys.snowflake.ColossusGuild;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.modals.Modal;
import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.events.repliable.RepliableEvent;
import dev.ryanland.colossus.sys.snowflake.ColossusMember;
import dev.ryanland.colossus.sys.snowflake.ColossusUser;

public final class MessageCommandEvent extends CommandEvent {

    private Command command;
    private SubCommandHolder headSubCommandHolder;
    private SubCommandHolder nestedSubCommandHolder;
    private ParsedArgumentMap parsedArgs;
    private final MessageReceivedEvent event;

    public MessageCommandEvent(MessageReceivedEvent event) {
        this.event = event;
        repliableEvent = new RepliableEvent() {
            @Override
            public ColossusUser getUser() {
                return new ColossusUser(event.getAuthor());
            }

            @Override
            public ColossusMember getMember() {
                return new ColossusMember(event.getMember());
            }

            @Override
            public ColossusGuild getGuild() {
                return new ColossusGuild(event.getGuild());
            }

            @Override
            public void reply(String message, boolean ephemeral) {
                event.getMessage().reply(message).queue();
            }

            @Override
            public void reply(MessageEmbed embed, boolean ephemeral) {
                event.getMessage().replyEmbeds(embed).queue();
            }

            @Override
            public void reply(PresetBuilder embed) {
                event.getMessage().replyEmbeds(embed.embed())
                    .setComponents(embed.getActionRows())
                    .setContent(embed.getContent())
                    .queue(embed::addComponentRowListeners);
            }

            @Override
            public void reply(Modal modal, CommandConsumer<ModalSubmitEvent> action) {
                throw new IllegalStateException("Message commands do not support replying with modals");
            }
        };
    }

    @Override
    public Command getCommand() {
        return command;
    }

    @Override
    public SubCommandHolder getHeadSubCommandHolder() {
        return headSubCommandHolder;
    }

    @Override
    public SubCommandHolder getNestedSubCommandHolder() {
        return nestedSubCommandHolder;
    }

    @Override
    public String getName() {
        if (event.getMessage().getContentRaw() == null)
            throw new IllegalStateException("You must have the Message Content intent enabled to use this feature");
        try {
            return event.getMessage().getContentRaw().split("\\s+")[0].substring(Config.getString("message_commands.prefix").length()).toLowerCase();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public void setHeadSubCommandHolder(SubCommandHolder subCommandHolder) {
        this.headSubCommandHolder = subCommandHolder;
    }

    @Override
    public void setNestedSubCommandHolder(SubCommandHolder subCommandHolder) {
        this.nestedSubCommandHolder = subCommandHolder;
    }

    @Override
    public ParsedArgumentMap getParsedArgs() {
        return parsedArgs;
    }

    @Override
    public void setParsedArgs(ParsedArgumentMap parsedArgs) {
        this.parsedArgs = parsedArgs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getArgument(String id) {
        return (T) parsedArgs.get(id);
    }

    @Override
    public boolean isFromGuild() {
        return event.isFromGuild();
    }

    @Override
    public DiscordLocale getUserLocale() {
        return Colossus.DEFAULT_LOCALE; // we cant get the user locale with message commands, so return the default
    }

    @Override
    public JDA getJDA() {
        return event.getJDA();
    }

    @Override
    public MessageChannel getChannel() {
        return event.getChannel();
    }

    public Message getMessage() {
        return event.getMessage();
    }

    @Override
    public MessageReceivedEvent getEvent() {
        return event;
    }

    @Override
    public ColossusUser getUser() {
        return new ColossusUser(event.getAuthor());
    }

    @Override
    public ColossusMember getMember() {
        return new ColossusMember(event.getMember());
    }

    @Override
    public ColossusGuild getGuild() {
        return new ColossusGuild(event.getGuild());
    }
}
