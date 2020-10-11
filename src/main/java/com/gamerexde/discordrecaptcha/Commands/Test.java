package com.gamerexde.discordrecaptcha.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Test extends Command {
    public Test() {
        this.name = "test";
        this.help = "Boilerplate class for creating new commands.";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().toLowerCase().split(" ");

        // These are some objects that I normally call when creating new
        // commands...
        //
        //User user = event.getSelfUser();
        //Member member = event.getMember();
        //Guild guild = event.getGuild();



    }
}
