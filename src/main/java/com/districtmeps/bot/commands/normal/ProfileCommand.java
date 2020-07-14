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

package com.districtmeps.bot.commands.normal;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * UserInfoCommand
 */
public class ProfileCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        
        String userId = null;
        if(args.size() > 0){
            if(event.getMessage().getMentionedUsers().size() != 1){
                event.getChannel().sendMessage("Incorrect arguments\nPlease check `" + Constants.PREFIX + "help " + getInvoke() + "`\nTip: Only mention one person. No mention needed for youself").queue();
                return;
            } else {
                userId = event.getMessage().getMentionedUsers().get(0).getId();
            }
        } else {
            userId = event.getAuthor().getId();
        }

        
        String insta = APIHelper.getInstaInfo(userId).get("insta");
        User user = event.getJDA().getUserById(userId);
        Member member = event.getGuild().getMember(user);
        String coins = "" + APIHelper.getCoins(user.getId());
        String instagram = insta == "null" ? "N/A" : insta;

        MessageEmbed embed = EmbedUtils.defaultEmbed()
            .setColor(member.getColor())
            .setThumbnail(user.getEffectiveAvatarUrl())
            .addField("Username#Discriminator", String.format("%#s", user), true)
            .addField("Display Name", member.getEffectiveName(), true)
            .addField("Coins", coins, true)
            .addField("Instagram", instagram, true)
            .addField("User Id + Mention", String.format("%s {%s}", user.getId(), user.getAsMention()), true)
            .addField("Account Created", user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
            .addField("Guild Joined", member.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
            .addField("Online Status", member.getOnlineStatus().name().toLowerCase().replaceAll("_", " "), true)
            .addField("Bot Account", user.isBot() ? "Yes" : "No", true)
            .build();

        event.getChannel().sendMessage(embed).queue();

    }

    @Override
    public String getHelp() {
        return "Displays information about a user.\n" +
        "Usage: `" + Constants.PREFIX + getInvoke() + " <user name/@user/user id>`";
    }

    @Override
    public String getInvoke() {
        return "profile";
    }

    @Override
    public int getType() {
        return 3;
    }
}