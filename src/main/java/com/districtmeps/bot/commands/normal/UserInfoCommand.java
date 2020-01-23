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
public class UserInfoCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        
        if(args.isEmpty()){
            event.getChannel().sendMessage("Missing arguments, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        String joined =  String.join("", args);
        List<User> foundUsers = FinderUtil.findUsers(joined, event.getJDA());

        if(foundUsers.isEmpty()){
            
            List<Member> foundMembers = FinderUtil.findMembers(joined, event.getGuild());

            if(foundMembers.isEmpty()){
                event.getChannel().sendMessage("No users found for `" + joined + "`").queue();
                return;
            }

            foundUsers = foundMembers.stream().map(Member::getUser).collect(Collectors.toList());
        }

        User user = foundUsers.get(0);
        Member member = event.getGuild().getMember(user);
        String coins = "" + APIHelper.getCoins(user.getId());

        MessageEmbed embed = EmbedUtils.defaultEmbed()
            .setColor(member.getColor())
            .setThumbnail(user.getEffectiveAvatarUrl())
            .addField("Username#Discriminator", String.format("%#s", user), true)
            .addField("Display Name", member.getEffectiveName(), true)
            .addField("Coins", coins, true)
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
        "Usage: `" + Constants.PREFIX + getInvoke() + " [user name/@user/user id]`";
    }

    @Override
    public String getInvoke() {
        return "userinfo";
    }

    @Override
    public int getType() {
        return 3;
    }
}