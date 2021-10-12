package net.ryanland.colossus.bot.events.logs;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.message.builders.PresetType;
import net.ryanland.colossus.sys.message.webhooks.WebhookHandler;
import net.ryanland.colossus.util.NumberUtil;

import java.time.Instant;

public class GuildTraffic extends ListenerAdapter {

    private static final String WEBHOOK_URL = WebhookHandler.getWebhooks().getGuildTraffic();
    private static final WebhookClient WEBHOOK_CLIENT = WebhookClient.withUrl(WEBHOOK_URL);

    public void onGuildJoin(GuildJoinEvent event) {
        sendEmbed(event.getGuild(), true);
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        sendEmbed(event.getGuild(), false);
    }

    private static void sendEmbed(Guild guild, boolean joined) {
        String header;
        String action;
        int color;

        if (joined) {
            header = "Join";
            action = "joined";
            color = PresetType.SUCCESS.getColor();
        } else {
            header = "Leave";
            action = "left";
            color = PresetType.ERROR.getColor();
        }

        WebhookEmbed embed = new WebhookEmbedBuilder()
            .setColor(color)
            .setTitle(new WebhookEmbed.EmbedTitle("Guild " + header, null))
            .setThumbnailUrl(guild.getIconUrl())
            .setTimestamp(Instant.now())
            .setDescription(String.format("**%s** has __%s__ the following guild:\n\u200b", Colossus.getSelfUser().getName(), action))
            .addField(new WebhookEmbed.EmbedField(true, "Name", guild.getName()))
            .addField(new WebhookEmbed.EmbedField(true, "ID", guild.getId()))
            .addField(new WebhookEmbed.EmbedField(true, "Member Count", NumberUtil.format(guild.getMemberCount())))
            .build();

        WEBHOOK_CLIENT.send(embed);
    }
}
