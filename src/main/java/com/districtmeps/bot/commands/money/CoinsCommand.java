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

package com.districtmeps.bot.commands.money;

import java.util.List;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * CoinsCommand
 */
public class CoinsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        EmbedBuilder builder = EmbedUtils.defaultEmbed();
        User user = null;

        if (event.getMessage().getMentionedUsers().size() == 1) {
            user = event.getMessage().getMentionedUsers().get(0);

            builder.setDescription(user.getName() + "#" + user.getDiscriminator() + " has `" + APIHelper.getCoins(user.getId())
                    + "` coins <a:coin:630157923141812245>");
            builder.setTitle("Coins");
        } else if (event.getMessage().getMentionedUsers().size() == 0) {

            builder.setDescription("You have `" + APIHelper.getCoins(event.getAuthor().getId())
                    + "` coins <a:coin:630157923141812245>");
            builder.setTitle("Coins");
        } else {
            event.getChannel().sendMessage("Something is not right, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        event.getChannel().sendMessage(builder.build()).queue();

    }

    @Override
    public String getHelp() {
        return "Send the amount of coins you have\n" + "Usage: `" + Constants.PREFIX + getInvoke()
                + " [user name/@user/user id]`";
    }

    @Override
    public String getInvoke() {
        return "coins";
    }

    @Override
    public int getType() {
        return 8;
    }

}