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

import java.util.List;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.ICommand;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * PreviewDMCommand
 */
public class PreviewDMCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Role> mentions = event.getMessage().getMentionedRoles();
        List<Member> membersWithRole = null;
        Guild guild = event.getGuild();

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage(
                    "You need the `Manage Server` permission to send a dm\nI will let you through because it is a preview")
                    .queue();
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

        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("A message from " + event.getAuthor().getName() + " aka " + event.getMember().getNickname()
                        + " to members with role: `" + mentionedRole.getName() + "`");
        builder.setDescription("\n" + message);
        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Allows you to see a preview of the DM the bot will send to users of a mentioned role\n" + "Usage: `"
                + Constants.PREFIX + getInvoke()
                + " <Role Mention> <Message>` (You can add `<br>` for a new line)\nRequires the `Manage Server` permission";
    }

    @Override
    public String getInvoke() {
        return "previewdm";
    }

    @Override
    public int getType() {
        return 2;
    }

}