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

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class PinsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("You need the `Manage Messages` permission to use this command").queue();

            return;
        }

        System.out.println(args.size());
        if (args.size() > 0) {

            System.out.println(event.getMessage().getMentionedChannels().size());
            if (event.getMessage().getMentionedChannels().size() == 1) {
                channel = event.getMessage().getMentionedChannels().get(0);
            } else {
                event.getChannel().sendMessage(
                        "I do not have permission to view the pinned in that channel. Tip: You do not need to mention a channel the see the pins in the channel you currently are in.")
                        .queue();
                return;
            }
        }

        // channel
        List<Message> pinned;
        try {
            pinned = channel.retrievePinnedMessages().complete();
            event.getChannel().sendMessage("There are " + pinned.size() + " pins in " + channel.getAsMention()).queue();
        } catch (InsufficientPermissionException e) {
            event.getChannel().sendMessage("I do not have permission to see the pins of this channel, or the channel no longer exists.").queue();
        }
        
        

     }

     @Override
     public String getHelp() {
         return "Send the ammount of pins in the current or mentioned channel\n" + "Usage: `" + Constants.PREFIX + getInvoke() + " [channel mention]`\n"
         + "Requires `Manage Messages` permission";
     }

     @Override
     public String getInvoke() {
         return "pins";
     }

     @Override
     public int getType() {
         return 2;
     }
    
}