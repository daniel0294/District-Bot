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

import com.districtmeps.bot.music.GuildMusicManager;
import com.districtmeps.bot.music.PlayerManager;
import com.districtmeps.bot.objects.ICommand;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * StopCommand
 */
public class StopCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);

        event.getChannel().sendMessage("Stopping the player and clearing the queue").queue();

    }

    @Override
    public String getHelp() {
        return "Stops the music player";
    }

    @Override
    public String getInvoke() {
        return "stop";
    }

    @Override
    public int getType() {
        return 4;
    }
    
}