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

package com.districtmeps.bot.commands.normal;

import java.time.Instant;
import java.util.List;

import com.districtmeps.bot.objects.ICommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UploadHelpCommand implements ICommand {
    private final Logger logger = LoggerFactory.getLogger(UploadHelpCommand.class);

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        
        List<Attachment> att = event.getMessage().getAttachments();

        if(att.size() != 1){
            event.getChannel().sendMessage("Please upload only one photo").queue();
            return;
        }

        if(args.size() < 1){
            event.getChannel().sendMessage("Please also send the link").queue();
            return;
        }

        String texts = "";
        for (String text : args) {
            if(!checkLink(text)){
                event.getChannel().sendMessage("Please only send valid links").queue();
                return;
            }

            texts += (texts + "\n");
        }

        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
        builder.setFooter(String.format("Image posted by %#s", event.getAuthor()));
        builder.setTimestamp(Instant.now());
        builder.setDescription("[Sauce](" + args.get(0) + ")");
        builder.setImage(att.get(0).getProxyUrl());


        event.getChannel().sendMessage(builder.build()).queue();
        event.getMessage().delete().queue();
        // event.getChannel().sendMessage(att.get(0).getProxyUrl()).queue();
        
        logger.info(texts);

        

    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "upload";
    }

    @Override
    public int getType() {
        return 3;
    }

    private boolean checkLink(String link){
        return link.startsWith("https://") || link.startsWith("http://");
    }
    
}
