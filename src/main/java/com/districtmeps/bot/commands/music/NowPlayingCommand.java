/**
 *      Copyright 2020-2021 Daniel Sanchez
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
import java.util.concurrent.TimeUnit;

import com.districtmeps.bot.music.GuildMusicManager;
import com.districtmeps.bot.music.PlayerManager;
import com.districtmeps.bot.objects.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * NowPlayingCommand
 */
public class NowPlayingCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = musicManager.player;

        if(player.getPlayingTrack() == null){
            channel.sendMessage("The Player is not playing any song").queue();
            return;
        }

        AudioTrackInfo info = player.getPlayingTrack().getInfo();
        channel.sendMessage(EmbedUtils.embedMessage(String.format(
            "**Playing** [%s] \n(%s)\n%s %s - %s",
             info.title, 
             info.uri,
             player.isPaused() ? "\u23F8" : "▶",
             formatTime(player.getPlayingTrack().getPosition()),
             formatTime(player.getPlayingTrack().getDuration())
        )).setThumbnail(getYoutubeThumbnail(info.uri)).build()).queue();
    }

    private String getYoutubeThumbnail(String url){
        if(!url.startsWith("https://www.youtube.com")){
            return null;
        }

        int equals = url.indexOf("=");
        url = url.substring(equals + 1);
        String fullUrl = "https://img.youtube.com/vi/" + url + "/hqdefault.jpg";
        System.out.println(fullUrl);
        return fullUrl;
    }

    @Override
    public String getHelp() {
        return "Shows the current playing Track";
    }

    @Override
    public String getInvoke() {
        return "np";
    }

    @Override
    public int getType() {
        return 4;
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    
}