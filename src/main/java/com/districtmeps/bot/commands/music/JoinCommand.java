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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * JoinCommand
 */
public class JoinCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if(audioManager.isConnected()){
            channel.sendMessage("I'm already connected to a Voice Channel").queue();
            return;
        }

        GuildVoiceState memberVoiceState =  event.getMember().getVoiceState();
        
        if(!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("Please join a Voice Channel first").queue();
            return;
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfMember = event.getGuild().getSelfMember();

        if(!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)){
            channel.sendMessageFormat("I am missing the join %s", voiceChannel).queue();
            return;
        }

        audioManager.openAudioConnection(voiceChannel);
        channel.sendMessage("Joining your Voice Channel").queue();

    }

    @Override
    public String getHelp() {
        return "Makes bot join your Voice Channel";
    }

    @Override
    public String getInvoke() {
        return "join";
    }

    @Override
    public int getType() {
        return 4;
    }

    
}