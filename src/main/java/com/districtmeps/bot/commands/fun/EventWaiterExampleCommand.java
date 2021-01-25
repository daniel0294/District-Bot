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

package com.districtmeps.bot.commands.fun;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.districtmeps.bot.objects.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * EventWaiterExampleCommand
 */
public class EventWaiterExampleCommand implements ICommand { 

    private static final String EMOJI = "\uD83D\uDC4D";
    private static final String emoteId = "586118085812027402";
    private EventWaiter waiter;

    public EventWaiterExampleCommand(EventWaiter waiter){
        this.waiter = waiter;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();

        channel.sendMessage("Please react with " + EMOJI).queue((message) -> {
            message.addReaction(EMOJI).queue();
            message.addReaction(event.getJDA().getEmoteById(emoteId)).queue();
            initWaiter(message.getIdLong(), channel.getIdLong(), event.getJDA().getShardManager());
        });

        channel.sendMessage("test").queue();

    }

    private void initWaiter(long messageId, long channelId, ShardManager shardManager) {
        waiter.waitForEvent(GuildMessageReactionAddEvent.class, (event) -> {
            ReactionEmote emote = event.getReactionEmote();
            User user = event.getUser();

            return !user.isBot() && event.getMessageIdLong() == messageId && !emote.isEmote()
                    && EMOJI.equals(emote.getName());
            },
            (event)->{
                TextChannel channel = shardManager.getTextChannelById(channelId);
                User user = event.getUser();

                channel.sendMessageFormat("%#s was the first to react with `" + EMOJI + "` to the message", user).queue();
            },
            10, TimeUnit.SECONDS,
            ()->{
                TextChannel channel = shardManager.getTextChannelById(channelId);

                channel.sendMessage("I stopped listening for a reaction").queue();
            }
        );  
    }

    @Override
    public String getHelp() {
        return "Exaple for an event waiter";
    }

    @Override
    public String getInvoke() {
        return "wait";
    }

    @Override
    public int getType() {
        return 5;
    }
    
}