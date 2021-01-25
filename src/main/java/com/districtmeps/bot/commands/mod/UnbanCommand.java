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

package com.districtmeps.bot.commands.mod;

import java.util.List;
import java.util.stream.Collectors;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.ICommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * UnbanCommand
 */
public class UnbanCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();

        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("You do not have permission to Unban members.").queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("I do not have permission to Unban members.").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        String joined = String.join(" ", args);

        event.getGuild().retrieveBanList().queue((bans) -> {

            List<User> goodUsers = bans.stream().filter((ban) -> isCorrectUser(ban, joined)).map(Guild.Ban::getUser)
                    .collect(Collectors.toList());


            if (goodUsers.size() < 1){
                channel.sendMessage("This user is not banned").queue();
                return;
            }

            User target = goodUsers.get(0);

            String mod = String.format("%#s", event.getAuthor());
            String bannedUser = String.format("%#s", target);

            event.getGuild().unban(target).reason("Unbanned by " + mod).queue();

            channel.sendMessage("User " + bannedUser + " unbanned").queue();

        });

    }

    @Override
    public String getHelp() {
        return "Unbans a member from the server\n" + "Usage: `" + Constants.PREFIX + getInvoke()
                + " <username/user id/username#disc>`";
    }

    @Override
    public String getInvoke() {
        return "unban";
    }

    @Override
    public int getType() {
        return 2;
    }
    

    private boolean isCorrectUser(Guild.Ban ban, String arg) {
        User banned = ban.getUser();

        return banned.getName().equalsIgnoreCase(arg) || banned.getId().equals(arg)
                || String.format("%#s", banned).equalsIgnoreCase(arg);
    }

    

}