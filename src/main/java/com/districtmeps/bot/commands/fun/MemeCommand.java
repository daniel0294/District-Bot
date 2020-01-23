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

package com.districtmeps.bot.commands.fun;

import java.util.List;

import com.districtmeps.bot.objects.ICommand;
import com.fasterxml.jackson.databind.JsonNode;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MemeCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async((json) -> { 
            JsonNode data = json.get("data");
            String url = data.get("image").asText();
            MessageEmbed embed = EmbedUtils.embedImage(url)
                    .setTitle(data.get("title").asText(), data.get("url").asText())
                    .build();
            //TODO: Make a permission check to see if the bot can send embeds if not, send plain text
            event.getChannel().sendMessage(embed).queue();
        });
    }

    @Override
    public String getHelp() {
        return "Shows you a random meme.";
    }

    @Override
    public String getInvoke() {
        return "meme";
    }

    @Override
    public int getType() {
        return 5;
    }

}