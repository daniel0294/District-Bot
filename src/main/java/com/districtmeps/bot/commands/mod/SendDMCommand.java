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

package com.districtmeps.bot.commands.mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.ICommand;
import com.districtmeps.bot.objects.SyncronizeObj;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * SendDMCommand
 */
public class SendDMCommand implements ICommand {
    SyncronizeObj sync = new SyncronizeObj();
    int count = 0;
    int goodSends = 0;
    int badSends = 0;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        count = 0;
        goodSends = 0;
        badSends = 0;

        List<Role> mentions = event.getMessage().getMentionedRoles();
        List<Member> membersWithRole = null;
        Guild guild = event.getGuild();
        Map<User, String> failedSendsMap = new HashMap<>();

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage("You need the `Manage Server` permission to send a dm").queue();
            return;
        }

        if (args.size() < 2 || mentions.isEmpty()) {
            event.getChannel()
                    .sendMessage("Missing arguments, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        Role mentionedRole = mentions.get(0);
        if (!guild.getRoles().contains(mentionedRole)) {
            event.getChannel().sendMessage("That Role is not from this server").queue();
            return;
        }

        membersWithRole = guild.getMembersWithRoles(mentionedRole);
        if (membersWithRole.size() < 1) {
            event.getChannel().sendMessage("No one has this role").queue();
            return;
        }

        String reason = String.join(" ", args.subList(1, args.size()));
        String[] lines = reason.split("<br>");
        String message = String.join("\n", lines);

        // System.out.println(message);
        // event.getChannel().sendMessage(message).queue();

        membersWithRole.forEach((mem) -> {
            mem.getUser().openPrivateChannel().queue((channel) -> {
                EmbedBuilder builder = EmbedUtils.defaultEmbed()
                        .setTitle("A message from " + event.getAuthor().getName() + " aka "
                                + event.getMember().getNickname() + " to members with role: `" + mentionedRole.getName()
                                + "`");
                builder.setDescription("\n" + message);
                channel.sendMessage(builder.build()).queue(success -> {
                    goodSends++;
                    count++;
                }, failure -> {
                    count++;
                    badSends++;
                    // System.out.println(failure.getMessage());
                    failedSendsMap.put(mem.getUser(), failure.getMessage());
                    // System.out.println(failedSendsMap.size());
                });
                
                // System.out.println(count);
            });

            sync.doNotify();
        });

        System.out.println(count);
        System.out.println(membersWithRole.size());
        while (count < membersWithRole.size()) {
            System.out.println("test");
            sync.doWait(3000);

        }
        event.getChannel().sendMessage("Completed..sent message: \n`" + message + "`\n to " + (membersWithRole.size() - badSends) + "/"
                + membersWithRole.size() + " users with role: `" + mentionedRole.getName() + "`").queue();
        
        // System.out.println(count);
        // System.out.println(failedSendsMap.size());
        if (failedSendsMap.size() > 0) {
            EmbedBuilder builder = EmbedUtils.defaultEmbed().setTitle("List of Users I could not send the message to");
            builder.addField("Amount of failed messages", "" + badSends, false);
            failedSendsMap.forEach((k, v) -> {
                builder.appendDescription("\n" + k.getAsMention() + " | | " + v);
            });

            event.getChannel().sendMessage(builder.build()).queue();
        }
        

    }

    @Override
    public String getHelp() {
        return "Allows you to send a DM using the bot to users of a mentioned role\n" + "Usage: `" + Constants.PREFIX
                + getInvoke()
                + " <Role Mention> <Message>` (You can add `<br>` for a new line)\nRequires the `Manage Server` permission";
    }

    @Override
    public String getInvoke() {
        return "senddm";
    }

    @Override
    public int getType() {
        return 2;
    }

}