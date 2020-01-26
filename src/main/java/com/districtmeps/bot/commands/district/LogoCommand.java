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

import com.districtmeps.bot.config.Config;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * LogoCommajnd
 */
public class LogoCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!event.getGuild().getId().equals("296844096813793281")){
            event.getChannel().sendMessage("This command can only be used in the District Discord Server");
            return;
        }

        EmbedBuilder builder = EmbedUtils.defaultEmbed();
        builder.setTitle("District Logos").setThumbnail("https://www.districtmeps.com/media/img/whitemountaincropped.png").setDescription(Config.getInstance().getString("distlogo"));

        event.getChannel().sendMessage(builder.build()).queue();

    }

    @Override
    public String getHelp() {
        return "Sends the District Logo Mega Link. Can only be used in the District Server";
    }

    @Override
    public String getInvoke() {
        return "logo";
    }

    @Override
    public int getType() {
        return 9;
    }

    
}