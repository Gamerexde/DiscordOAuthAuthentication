package com.gamerexde.discordrecaptcha;

import com.gamerexde.discordrecaptcha.Commands.Verify;
import com.gamerexde.discordrecaptcha.Config.Config;
import com.gamerexde.discordrecaptcha.Database.Database;
import com.gamerexde.discordrecaptcha.Database.MySQL;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class Bot {
    public static Bot instance;
    public Config config;
    public JDA jda;
    private Database db;

    public Bot() {
        instance = this;

        loadConfig();
        initDatabase();

        CommandClientBuilder jdaUtilities = new CommandClientBuilder();
        JDABuilder builder = JDABuilder.createLight(getConfig().getString("serverToken"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);

        jdaUtilities.setOwnerId(getConfig().getString("serverOwnerID"));
        jdaUtilities.setPrefix(getConfig().getString("serverPrefix"));
        jdaUtilities.setActivity(Activity.watching("En desarollo..."));

        jdaUtilities.addCommands(
                new Verify()
        );

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);

        CommandClient client = jdaUtilities.build();

        try {
            builder.addEventListeners(client);

            // TODO: Also implement eventlisteners.
            //builder.addEventListeners(new XPListener());

            jda = builder.build();
            jda.awaitReady();

        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void initDatabase() {
        this.db = new MySQL();
        this.db.load();
    }

    public Database getDB() {
        return this.db;
    }

    public static Bot getInstance() {
        return instance;
    }

    public Config getConfig() {
        return config;
    }

    public void loadConfig() {
        try {
            if (!new File("config.json").exists()) {
                System.out.println("[DiscordOAuth] El archivo config.json no existe, crealo...");
                config = new Config(new File(getClass().getClassLoader().getResource("config.json").getFile()));
            } else {
                System.out.println("[DiscordOAuth] Se ha cargado el archivo config.json!");
                config = new Config(new File("config.json"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
