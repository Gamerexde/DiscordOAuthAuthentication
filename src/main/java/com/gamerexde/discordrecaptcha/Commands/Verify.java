package com.gamerexde.discordrecaptcha.Commands;

import com.gamerexde.discordrecaptcha.Bot;
import com.gamerexde.discordrecaptcha.Database.Database;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;

public class Verify extends Command {
    public Verify() {
        this.name = "verify";
        this.help = "Verify your account in our website.";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().toLowerCase().split(" ");

        String userID = event.getMember().getUser().getId();
        Member member = event.getMember();
        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();

        Database db = Bot.getInstance().getDB();

        if (db.verifyUser(userID)) {
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Verified!");
            eb.setDescription("You have been verified correctly! The user role will be asigned to you shortly...");
            eb.setColor(Color.PINK);
            eb.setFooter("DiscordOAuth | By Gamerexde", event.getGuild().getIconUrl());

            channel.sendMessage(eb.build()).queue();

            guild.addRoleToMember(member.getIdLong(), guild.getRoleById(762360801599553537L)).queue();
        } else {
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("You are not verified.");
            eb.setDescription("Hmm, it seems that you are not verified... Please visit http://127.0.0.1/discord/join to complete the verification process in our discord server.");
            eb.setColor(Color.PINK);
            eb.setFooter("DiscordOAuth | By Gamerexde", event.getGuild().getIconUrl());

            channel.sendMessage(eb.build()).queue();
        }
    }
}
