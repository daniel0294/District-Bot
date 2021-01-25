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
import com.districtmeps.bot.music.TrackScheduler;
import com.districtmeps.bot.objects.ICommand;

 import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

 /**
  * ShuffleCommand
  */
 public class ShuffleCommand implements ICommand {

     @Override
     public void handle(List<String> args, GuildMessageReceivedEvent event) {

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;

        if(scheduler.getQueue().isEmpty()){
            event.getChannel().sendMessage("The queue is currently empty!").queue();
            return;
        }

        scheduler.shuffle();
        event.getChannel().sendMessage("The queue has been shuffled").queue();
     }

     @Override
     public String getHelp() {
         return "Shuffles the queue of songs";
     }

     @Override
     public String getInvoke() {
         return "shuffle";
     }

     @Override
     public int getType() {
         return 4;
     }
 
     
 }