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

import com.districtmeps.bot.music.GuildMusicManager;
import com.districtmeps.bot.music.PlayerManager;
import com.districtmeps.bot.objects.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

 /**
  * PauseCommand
  */
 public class PauseCommand implements ICommand {

     @Override
     public void handle(List<String> args, GuildMessageReceivedEvent event) {
         
         
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = musicManager.player;

        

        if(player.getPlayingTrack() == null){
            event.getChannel().sendMessage("The Player is not playing any song").queue();;
            return;
        }

        if(player.isPaused()){
            player.setPaused(false);
            event.getChannel().sendMessage("Player is no longer paused").queue();
            return;
        } else {
            player.setPaused(true);
            event.getChannel().sendMessage("Player is now paused").queue();
            return;
        }
        

     }

     @Override
     public String getHelp() {
         return "Pauses  and unpauses the current queue";
     }

     @Override
     public String getInvoke() {
         return "pause";
     }

     @Override
     public int getType() {
         return 4;
     }
 
     
 }