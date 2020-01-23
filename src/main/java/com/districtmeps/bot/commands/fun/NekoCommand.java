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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * NekoCommand
 */
public class NekoCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        WebUtils.ins.getJSONObject("https://nekos.life/api/v2/img/neko").async((json) -> {
            JsonNode jsonUrl = json.get("url");
            String url = jsonUrl.toString();

            url = url.substring(1, url.length() - 1);

            EmbedBuilder embed = EmbedUtils.embedImage(url).setTitle(getInvoke());
            // TODO: Make a permission check to see if the bot can send embeds if not, just
            // plain text
            event.getChannel().sendMessage(embed.build()).queue();

        });

    }

    @Override
    public String getHelp() {
        return "Sends a cute neko";
    }

    @Override
    public String getInvoke() {
        return "neko";
    }

    @Override
    public int getType() {
        return 5;
    }

}