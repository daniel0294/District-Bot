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

package com.districtmeps.bot.commands.district.meps;

import java.util.List;
import java.util.Map;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * MepInfoCommand
 */
public class MepInfoCommand implements ICommand {

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

        if(args.size() != 1){

            event.getChannel().sendMessage("Missing Arguements\nRefer to `" + Constants.PREFIX + getInvoke()).queue();
            return;
        }
        int mepId;

        try {
            mepId = Integer.parseInt(args.get(0));
        } catch (Exception e) {
            event.getChannel().sendMessage("Argument must be a number").queue();
            return;
        }

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        embed.setTitle("Information for Mep `#" + mepId + "`", "https://districtmeps.com/meps/" + mepId).setDescription("Please Wait");
        Message msg = event.getChannel().sendMessage(embed.build()).complete();
        
        
        


        Map<String, String> mep = APIHelper.getMep(mepId);

        if(mep.get("error").equals("true")){
            msg.getChannel().deleteMessageById(msg.getId()).queue();   
            event.getChannel().sendMessage(mep.get("message")).queue();
            return;
        }

        boolean completed;

        if(mep.get("completed").equals("0")){
            completed = false;
        } else {
            completed = true;
        }

        embed.setDescription(mep.get("name"))
        .addField("Server", event.getJDA().getGuildById(mep.get("server")).getName(), false)
        .addField("Description", mep.get("desc"), false)
        .addField("Parts", mep.get("parts"), true)
        .addField("Open Parts", mep.get("openParts"), true)
        .addField("Completed Parts", mep.get("doneParts"), true)
        .addField("Video Link", mep.get("videoLink"), false)
        .addField("Message Link", "https://discordapp.com/channels/" + mep.get("server") + "/" + mep.get("messageId"), false)
        .addField("Completed", completed ? "Yes" : "No", false);



        msg.editMessage(embed.build()).queue();

    }

    @Override
    public String getHelp() {
        return "Sends information about a mep\n" +
        "Usage: `" + Constants.PREFIX + getInvoke() + " <MEP id#>`";
    }

    @Override
    public String getInvoke() {
        return "mepinfo";
    }

    @Override
    public int getType() {
        return 9;
    }
 
     
 }