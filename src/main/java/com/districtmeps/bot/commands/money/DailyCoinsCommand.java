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

package com.districtmeps.bot.commands.money;

import java.util.List;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * DailyCoinsCommand
 */
public class DailyCoinsCommand implements ICommand {

    EmbedBuilder builder = EmbedUtils.getDefaultEmbed();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String message = APIHelper.dailyCoins(event.getAuthor().getId());

        builder.setTitle("Daily").setDescription(message);

        event.getChannel().sendMessage(builder.build()).queue();

    }

    @Override
    public String getHelp() {
        return "Claim your daily amount of coins\n" + "Usage: `" + Constants.PREFIX + getInvoke() + "`";
    }

    @Override
    public String getInvoke() {
        return "daily";
    }

    @Override
    public int getType() {
        return 8;
    }
}