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

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.ICommand;
import com.fasterxml.jackson.databind.JsonNode;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * OWOCommand
 */
public class OWOCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        String input = String.join(" ", args);
        


        WebUtils.ins.getJSONObject("https://nekos.life/api/v2/owoify?text=" + input).async((json) -> { 
            JsonNode jsonUrl = json.get("owo");
            String url = jsonUrl.toString();

            url = url.substring(1, url.length() - 1);

            EmbedBuilder embed = EmbedUtils.defaultEmbed().setTitle("OWO");
            embed.setDescription(url);
            channel.sendMessage(embed.build()).queue();

        });

    }

    @Override
    public String getHelp() {
        return "Owoifys any text you send through it\n" + 
                "Usage: `" + Constants.PREFIX + getInvoke() + " <message>`";
    }

    @Override
    public String getInvoke() {
        return "owoify";
    }

    @Override
    public int getType() {
        return 5;
    }

    
}