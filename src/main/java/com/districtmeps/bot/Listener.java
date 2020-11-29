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

package com.districtmeps.bot;

import com.districtmeps.bot.config.Config;
import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.sendpic.SendPic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

class Listener extends ListenerAdapter {

    private final CommandManager manager;
    private final Logger logger = LoggerFactory.getLogger(Listener.class);

    Listener(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info(String.format("Logged in as %#s\n", event.getJDA().getSelfUser()));

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().isWebhookMessage()) {
            return;
        }

        User author = event.getAuthor();
        String message = event.getMessage().getContentRaw();

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();

            logger.info(String.format("(%s) [%s] <%#s>: %s", guild.getName(), textChannel.getName(), author, message));

        } else if (event.isFromType(ChannelType.PRIVATE)) {
            logger.info(String.format("[PRIV]<%#s>: %s", author, message));
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getMessage().isWebhookMessage()) {
            return;
        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase(Constants.PREFIX + "shutdown")
                && event.getAuthor().getId().equals(Config.getInstance().getString("owner"))) {
            shutdown(event.getJDA());
            return;
        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase(Constants.PREFIX + "forceDaily")
                && event.getAuthor().getId().equals(Config.getInstance().getString("owner"))) {
            forceDaily();
            return;
        }

        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage()
                && event.getMessage().getContentRaw().startsWith(Constants.PREFIX)) {
            manager.handleCommand(event);
        }
        APIHelper.UserHasSpoken(event);

    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (event.getGuild().getId().equals("296844096813793281")) {
            event.getGuild().getSystemChannel()
                    .sendMessage("Welcome " + event.getUser().getAsMention() + "!!\nPlease tell us your instagram @"
                    + "\nUse d!updateinsta in <#402997922767306754> so everyone can know your instagram @.")
                    .queue();
            System.out.println("FKN TEST WORK");
            SendPic.start(event);
        }
    }

    private void shutdown(JDA jda) {
        jda.shutdown();
        System.exit(0);
    }

    private void forceDaily(){
        APIHelper.dailyReset();
    }
}
