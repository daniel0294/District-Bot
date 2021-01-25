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

import com.districtmeps.bot.objects.ICommand;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * LeaveCommand
 */
public class LeaveCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if(!audioManager.isConnected()){
            channel.sendMessage("I'm not connected to a Voice Channel").queue();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if(!voiceChannel.getMembers().contains(event.getMember())){
            channel.sendMessage("You have to be in the same Voice Channel as me to use this").queue();
            return;
        }

        audioManager.closeAudioConnection();
        channel.sendMessage("Disconnected from your Voice Channel").queue();

    }

    @Override
    public String getHelp() {
        return "Makes bot leave your Voice Channel";
    }

    @Override
    public String getInvoke() {
        return "leave";
    }

    @Override
    public int getType() {
        return 4;
    }

    
}