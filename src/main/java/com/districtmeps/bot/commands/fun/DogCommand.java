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

package com.districtmeps.bot.commands.fun;

import java.util.List;

import com.districtmeps.bot.objects.ICommand;
import com.fasterxml.jackson.databind.JsonNode;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * DogCommand
 */
public class DogCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        WebUtils.ins.getJSONObject("https://random.dog/woof.json").async((json) -> { 
            JsonNode jsonUrl = json.get("url");
            String url = jsonUrl.toString();

            url = url.substring(1, url.length() - 1);

            if (!checkComp(url)) {
                EmbedBuilder embed = EmbedUtils.embedImage(url);
                event.getChannel().sendMessage(embed.build()).queue();
                return;
            } else {
                handle(args, event);
                return;
            }
        });

    }

    @Override
    public String getHelp() {
        return "Shows you a random dog.";
    }

    @Override
    public String getInvoke() {
        return "dog";
    }

    @Override
    public int getType() {
        return 5;
    }

    private boolean checkComp(String url) {
        return url.toLowerCase().endsWith(".mp4") || url.toLowerCase().endsWith(".webm");
    }

}