/**
 *      Copyright 2020 Daniel Sanchez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.districtmeps.bot.commands.music;

import java.util.List;
import java.util.regex.Pattern;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.music.GuildMusicManager;
import com.districtmeps.bot.music.PlayerManager;
import com.districtmeps.bot.objects.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * VolumeCommand
 */
public class VolumeCommand implements ICommand {

    //TODO Save Volume for every server

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = musicManager.player;

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments, check `" + Constants.PREFIX + "help " + getInvoke()
                    + "`\nVolume right now is `" + player.getVolume() + "`").queue();
            return;
        }

        if (!checkNewCommand(event)) {
            return;
        }

        String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Constants.PREFIX), "")
                .split("\\s+");
        int num = Integer.parseInt(split[1]);

        player.setVolume(num);
        event.getChannel().sendMessage("Set volume to `" + num + '`').queue();
        

    }

    private boolean checkNewCommand(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Constants.PREFIX), "")
                .split("\\s+");
        // String rawMessage = event.getMessage().getContentRaw();

        if (split.length != 2) {
            event.getChannel().sendMessage("Please only enter a number after the command .").queue();
            return false;
        }

        int num;
        try {
            num = Integer.parseInt(split[1]);
        } catch (Exception e) {
            event.getChannel().sendMessage("Please only enter a number after the command.").queue();
            return false;
        }

        if(num < 10 || num > 100){
            event.getChannel().sendMessage("Number must be between 10 and 100").queue();
            return false;

        }

        return true;
    }

    @Override
    public String getHelp() {
        return "Allows you to change the volume of bot\n" + "Usage: " + Constants.PREFIX + getInvoke()
                + " <NUM> (10-100)";
    }

    @Override
    public String getInvoke() {
        return "volume";
    }

    @Override
    public int getType() {
        return 4;
    }

}