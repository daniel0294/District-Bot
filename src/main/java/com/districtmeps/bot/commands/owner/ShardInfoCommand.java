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

package com.districtmeps.bot.commands.owner;

import java.util.List;

import com.districtmeps.bot.config.Config;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * ShardInfoCommand
 */
public class ShardInfoCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getAuthor().getId().equals(Config.getInstance().getString("owner"))) {
            return;
        }

        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();

        builder.setDescription("This is shard #" + (1+ event.getJDA().getShardInfo().getShardId()) + "\n(not zero indexed)");
        builder.setTitle("Shard Info");

        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Sends Shard Info";
    }

    @Override
    public String getInvoke() {
        return "shardinfo";
    }

    @Override
    public int getType() {
        return 1;
    }

}