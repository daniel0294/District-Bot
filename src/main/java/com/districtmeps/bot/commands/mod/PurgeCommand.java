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

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.ICommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * PruneCommand
 */
public class PurgeCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("You need the `Manage Messages` permission to use this command").queue();

            return;
        }

        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("I need the `Manage Messages` permission for this command").queue();

            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Correct usage is: `" + Constants.PREFIX + getInvoke() + " <amount>`").queue();

            return;
        }

        int amount;
        String arg = args.get(0);

        try {
            amount = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
            channel.sendMessageFormat("`%s` is not a valid number", arg).queue();

            return;
        }

        if (amount < 2 || amount > 100) {
            channel.sendMessage("Amount must be at least 2 and at most 100").queue();

            return;
        }

        channel.getIterableHistory()
                .takeAsync(amount)
                .thenApplyAsync( (messages) ->{
                    List<Message> goodMessages = messages.stream()
                        .filter( (m) -> !m.getTimeCreated().isAfter(
                            OffsetDateTime.now().plus(2, ChronoUnit.WEEKS)))
                                .collect(Collectors.toList());

                    channel.purgeMessages(goodMessages);
                    return goodMessages.size();
                })
                .whenCompleteAsync((count, thr) -> {
                    channel.sendMessageFormat("Deleted `%d` messages", count).queue((message) ->{
                        message.delete().queueAfter(10, TimeUnit.SECONDS);
                    });
                }).exceptionally((thr)->{

                    String cause = "";
                    if(thr.getCause() != null){
                        cause = " caused by: " + thr.getCause().getMessage();

                    }

                    channel.sendMessageFormat("Error %s%s", thr.getMessage(), cause).queue();
                    return 0;
                });

    }

    @Override
    public String getHelp() {
        return "Clears the chat with the specified amount of messages.\n"
                +"Usage: `" + Constants.PREFIX + getInvoke() + " <amount>`";
    }

    @Override
    public String getInvoke() {
        return "purge";
    }

    @Override
    public int getType() {
        return 2;
    }

    
}