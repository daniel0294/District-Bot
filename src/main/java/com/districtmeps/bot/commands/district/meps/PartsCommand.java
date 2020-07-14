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

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * PartsCommand
 */
public class PartsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        // Dist and Test
        if (!event.getGuild().getId().equals("296844096813793281")
                && !event.getGuild().getId().equals("374386835327287306")) {
            event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
            return;
        }

        // Just Dist
        // if(!event.getGuild().getId().equals("296844096813793281")){
        // event.getChannel().sendMessage("This command can only be used in the District
        // Discord Server").queue();
        // return;
        // }

        if (args.size() != 1) {

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

        EmbedBuilder embed = EmbedUtils.defaultEmbed();

        embed.setTitle("Parts Information for Mep `#" + mepId + "`", "https://districtmeps.com/meps/" + mepId)
                .setDescription("Please Wait");
        Message msg = event.getChannel().sendMessage(embed.build()).complete();

        Map<String, Object> partsAPI = APIHelper.getParts(mepId);

        if (partsAPI.get("error").toString().equals("true")) {
            msg.getChannel().deleteMessageById(msg.getId()).queue();
            event.getChannel().sendMessage(partsAPI.get("message").toString()).queue();
            return;
        }
        int count = Integer.parseInt(partsAPI.get("count").toString());

        embed.setDescription("`" + count + "` parts for MEP: " + partsAPI.get("mepName").toString());

        for (int i = 1; i <= count; i++) {
            if(i % 25 == 0){
                if(i == 25){
                    embed.setDescription("`" + (i - 24) + " - " + (i - 1) + "` parts for MEP: " + partsAPI.get("mepName").toString());
                } else {
                    embed.setDescription("`" + (i - 25) + " - " + (i - 1) + "` parts for MEP: " + partsAPI.get("mepName").toString());
                }
                
                event.getChannel().sendMessage(embed.build()).queue();
                embed.clearFields();
                
                System.out.println("Reached at " + i);
            }

            @SuppressWarnings("unchecked")
            Map<String, String> part = ((Map<String, String>) partsAPI.get("" + i));

            String status = "Open";

            if (part != null) {
                status = "Taken";
                status = part.get("done").equals("0") ? status : "Completed";

                status += "\nUser: " + event.getJDA().getUserById(part.get("userId")).getAsMention();
            }

            embed.addField("Part " + i, "Status: " + status, false);
        }
        msg.delete().queue();
        embed.setDescription("`" + count + "` parts for MEP: " + partsAPI.get("mepName").toString());
        event.getChannel().sendMessage(embed.build()).queue();


    }

    
    @Override
    public String getHelp() {
        return "Sends a list of parts for a mep\n" + "Usage: `" + Constants.PREFIX + getInvoke() + " <MEP id#>`";
    }

    @Override
    public String getInvoke() {
        return "parts";
    }

    @Override
    public int getType() {
        return 9;
    }

}