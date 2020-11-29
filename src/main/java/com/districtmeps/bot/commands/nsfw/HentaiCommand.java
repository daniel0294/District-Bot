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

package com.districtmeps.bot.commands.nsfw;

import java.util.List;

import com.districtmeps.bot.objects.ICommand;
import com.fasterxml.jackson.databind.JsonNode;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * HentaiCommand
 */
public class HentaiCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String[] links = { "femdom", "classic", "les", "hololewd", "lewdk", "keta", "feetg", "nsfw_neko_gif", "erofeet",
                "erok", "eroyuri", "kuni", "tits", "pussy_jpg", "cum_jpg", "pussy", "lewdkemo", "lewd", "cum",
                "smallboobs", "Random_hentai_gif", "boobs", "solog", "bj", "yuri", "trap", "anal", "blowjob", "blowjob",
                "gasm", "hentai", "futanari", "ero", "solo", "pwankg", "eron", "erokemo"};

        if (!event.getChannel().isNSFW()) {
            event.getChannel().sendMessage("Channel must be nsfw").queue();
            return;
        }

        int rand = (int) (Math.random() * links.length);

        WebUtils.ins.getJSONObject("https://nekos.life/api/v2/img/" + links[rand]).async((json) -> {
            JsonNode jsonUrl = json.get("url");
            String url = jsonUrl.toString();

            url = url.substring(1, url.length() - 1);

            EmbedBuilder embed = EmbedUtils.embedImage(url).setTitle(links[rand]);
            event.getChannel().sendMessage(embed.build()).queue();

        });

    }

    @Override
    public String getHelp() {
        return "Sends random eroge to a nsfw channel";
    }

    @Override
    public String getInvoke() {
        return "hentai";
    }

    @Override
    public int getType() {
        return 6;
    }

}