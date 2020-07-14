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

 package com.districtmeps.bot.commands.district;

import java.util.List;
import java.util.Map;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;
import com.districtmeps.bot.objects.SyncronizeObj;
import com.fasterxml.jackson.databind.JsonNode;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class UpdateInstaComand implements ICommand {

    // private final String CHANNELID = "732368141304463360"; //Test General
    private final String CHANNELID = "732391359926894673"; //District Insta Channel
    private static SyncronizeObj sync = new SyncronizeObj();
    private boolean check = true;
    private String pfp;
    private String messageId;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        //Dist and Test
        if(!event.getGuild().getId().equals("296844096813793281") && (!event.getGuild().getId().equals("374386835327287306"))){
            event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
            return;
        }

        //Just Dist
        // if(!event.getGuild().getId().equals("296844096813793281")){
        //     event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
        //     return;
        // }

        if (args.size() != 1){
            event.getChannel().sendMessage("Missing/incorrect arguments, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        String instaName = args.get(0);

        if(!checkInsta(instaName)){
            event.getChannel().sendMessage("This instagram account doesn't exist").queue();
            return;
        }

        // event.getChannel().sendMessage("This instagram account does exist").queue();

       

        Map<String, String> userInstaInfo = APIHelper.getInstaInfo(event.getAuthor().getId());
        // System.out.println(userInstaInfo.get("insta").equals(null));

        if(userInstaInfo.get("messageId").equals("null")){

            if(!event.getJDA().getTextChannelById(CHANNELID).canTalk()){
                event.getChannel().sendMessage("Please contact an admin - Can't Talk Error").queue();
                return;
            }


            EmbedBuilder builder = EmbedUtils.defaultEmbed();
            builder.setTitle("Instagram <:insta:732342758173573152>", "https://instagram.com/" + instaName)
            .setDescription("Instagram account for " + event.getAuthor().getAsMention())
            .addField("Insta @", "@" + instaName, false)
            .setColor(event.getMember().getColor())
            .setThumbnail(getInstaPfp(instaName));
            
            
            // event.getChannel().sendMessage(builder.build()).queue( (m) -> {
            //     messageId = m.getId();
            // });

            messageId = event.getJDA().getTextChannelById(CHANNELID).sendMessage(builder.build()).complete().getId();

            // event.getChannel().sendMessage(messageId).queue();

            APIHelper.saveInsta(event.getAuthor().getId(), instaName, messageId, event);

            event.getChannel().sendMessage("Your instagram tag has been saved and posted in the " + event.getJDA().getTextChannelById(CHANNELID).getAsMention() + " channel").queue();
            
        } else if(!userInstaInfo.get("insta").equals("null")){

            EmbedBuilder builder = EmbedUtils.defaultEmbed();
            builder.setTitle("Instagram <:insta:732342758173573152>", "https://instagram.com/" + instaName)
            .setDescription("Instagram account for " + event.getAuthor().getAsMention())
            .addField("Insta @", "@" + instaName, false)
            .setColor(event.getMember().getColor())
            .setThumbnail(getInstaPfp(instaName));

            boolean messageExists = true;
            Message message = null;
            try {
                message = event.getJDA().getTextChannelById(CHANNELID).retrieveMessageById(userInstaInfo.get("messageId")).complete();
            } catch (ErrorResponseException e) {
                messageExists = false;
            }
            

            


            
            if(messageExists){

                if(userInstaInfo.get("insta").equals(instaName)){
                    event.getChannel().sendMessage("Your Instagram is already saved, use `" + Constants.PREFIX + (new GetInstaCommand()).getInvoke() + "` to see your saved instagram name").queue();
                    return;
                }

                messageId = message.editMessage(builder.build()).complete().getId();
                APIHelper.saveInsta(event.getAuthor().getId(), instaName, messageId, event);
                event.getChannel().sendMessage("Your instagram tag has been saved and updated in the " + event.getJDA().getTextChannelById(CHANNELID).getAsMention() + " channel").queue();
                
            } else {
                messageId = event.getJDA().getTextChannelById(CHANNELID).sendMessage(builder.build()).complete().getId();
    
                APIHelper.saveInsta(event.getAuthor().getId(), instaName, messageId, event);
    
                event.getChannel().sendMessage("Your instagram tag has been saved and posted in the " + event.getJDA().getTextChannelById(CHANNELID).getAsMention() + " channel").queue();
                
            }
            
        }

    }

    @Override
    public String getHelp() {
        return "Attaches your instagram name to your discord in the server and sends it in the appropriate channel\n" + 
                "Usage: `" + Constants.PREFIX + getInvoke() + " <Instagram Name>`";
    }

    @Override
    public String getInvoke() {
        return "updateinsta";
    }

    @Override
    public int getType() {
        return 9;
    }

    private boolean checkInsta (String insta){
        WebUtils.ins.getJSONObject("https://www.instagram.com/" + insta + "/?__a=1").async((json) -> {
            JsonNode jsonUrl = json.get("logging_page_id");
            try {
                System.out.println(jsonUrl.toString());
                check = true;
                
            } catch (Exception e) {
                check = false;
            }
            sync.doNotify();
        });
        sync.doWait();
        return check;
    }

    public String getInstaPfp(String insta){
        WebUtils.ins.getJSONObject("https://www.instagram.com/" + insta + "/?__a=1").async((json) -> {
            pfp = json.get("graphql").get("user").get("profile_pic_url_hd").asText();
           

            sync.doNotify();
        });
        sync.doWait();
        return pfp;
    }

    
}