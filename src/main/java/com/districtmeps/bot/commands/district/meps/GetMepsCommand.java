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

package com.districtmeps.bot.commands.district.meps;

import java.util.List;
import java.util.Map;

import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * MepsCommand
 */
public class GetMepsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        //Dist and Test
        if(!event.getGuild().getId().equals("296844096813793281") && !event.getGuild().getId().equals("374386835327287306")){
            event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
            return;
        }

        //Just Dist
        // if(!event.getGuild().getId().equals("296844096813793281")){
        //     event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
        //     return;
        // }

        EmbedBuilder embed = EmbedUtils.defaultEmbed();

        embed.setTitle("Current Active Meps", "https://districtmeps.com/meps");
        
        Message msg = event.getChannel().sendMessage(embed.build()).complete();

        List<Map<String, String>> meps = APIHelper.getMeps();

        for (Map<String,String> mep : meps) {
            boolean completed = mep.get("completed").equals("1") ? true : false;
            
            String value= "Mep id: `" + mep.get("id") 
            + "`\nServer: " + event.getJDA().getGuildById(mep.get("server")).getName() 
            + "\nParts: `" + mep.get("parts") + "`";
            embed.addField(mep.get("name"), value, false);
        }
        

        msg.editMessage(embed.build()).queue();

        

    }

    @Override
    public String getHelp() {
        return "Sends a list of all the meps currently in the database";
    }

    @Override
    public String getInvoke() {
        return "meps";
    }

    @Override
    public int getType() {
        return 9;
    }

    
}