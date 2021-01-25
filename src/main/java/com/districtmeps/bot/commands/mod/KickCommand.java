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

package com.districtmeps.bot.commands.mod;

import java.util.List;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.ICommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * KickCommand
 */
public class KickCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member self = event.getGuild().getSelfMember();
        List<Member> mentions = event.getMessage().getMentionedMembers();

        if(args.isEmpty() || mentions.isEmpty()){
            event.getChannel().sendMessage("Missing arguments, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        Member target = mentions.get(0);
        String reason = String.join(" ", args.subList(1, args.size()));

        if (!member.hasPermission(Permission.KICK_MEMBERS) || !member.canInteract(target)){
            channel.sendMessage("You don't have permission to use this command").queue();
            return;
        }

        if (!self.hasPermission(Permission.KICK_MEMBERS) || !self.canInteract(target)){
            channel.sendMessage("I can't kick that user or I don't have the kick members permission").queue();
            return;
        }

        try {
            target.kick(String.format("Kicked by %#s, with reason: %s", event.getAuthor(), reason)).queue();

            channel.sendMessage("Success").queue();
        } catch (Exception e) {
            channel.sendMessage("Something went wrong. I can't kick this user").queue();
        }
        

    }

    @Override
    public String getHelp() {
        return "Kicks a user from the server\n" +
            "Usage: `" + Constants.PREFIX + getInvoke() + " <user> <reason>`";
    }

    @Override
    public String getInvoke() {
        return "kick";
    }

    @Override
    public int getType() {
        return 2;
    }

    

    
}