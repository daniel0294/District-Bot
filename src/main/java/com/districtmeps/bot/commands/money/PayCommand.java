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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * PayCommand
 */
public class PayCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder builder = EmbedUtils.defaultEmbed();
        builder.setTitle("Results");
        Member mentioned = null;
        int payCoins = 0;
        int userCoins = 0;
        String message = null;

        if(args.size() != 2){
            event.getChannel().sendMessage("Something is not right, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        if(event.getMessage().getMentionedMembers().size() != 1){
            event.getChannel().sendMessage("Something is not right, check `" + Constants.PREFIX + "help " + getInvoke() + "`\n\nHint: You must mention only `1` person").queue();
            return;
        }

        mentioned = event.getMessage().getMentionedMembers().get(0);
        if(mentioned.getUser().isBot() || mentioned.getUser().getId().equals(event.getAuthor().getId())){
            builder.setDescription("You can not send coins to a Bot, Webhook, or Yourself");

            event.getChannel().sendMessage(builder.build()).queue();
            return;
        }

        try {
            payCoins = Integer.parseInt(args.get(1));
        } catch (Exception e) {
            event.getChannel().sendMessage("Something is not right, check `" + Constants.PREFIX + "help " + getInvoke() + "`\n\nHint: Amount of coins must be a number").queue();
            return;
        }

        if(payCoins < 1){
            event.getChannel().sendMessage("You can't pay less than 1 coin").queue();
            return;
        }

        userCoins = APIHelper.getCoins(event.getAuthor().getId());
        if (payCoins > userCoins){
            builder.setDescription("You can't pay more coins that have.\nYou have `" + userCoins + "` coins. <a:coin:630157923141812245>");
            event.getChannel().sendMessage(builder.build()).queue();
            return;
        }


        if(APIHelper.payCoins(event.getAuthor().getId(), mentioned.getUser().getId(), payCoins)){
            message = "You sent `" + payCoins + "` to `" + mentioned.getUser().getName() + "#" + mentioned.getUser().getDiscriminator() + "`"; 
        } else {
            message = "Something went wrong";
        }
        

        
        builder.setDescription(message);

        event.getChannel().sendMessage(builder.build()).queue();



    }

    @Override
    public String getHelp() {
        return "Send coins to another User\n" + "Usage: `" + Constants.PREFIX + getInvoke()
                + " [mention] [# of coins]`";
    }

    @Override
    public String getInvoke() {
        return "pay";
    }

    @Override
    public int getType() {
        return 8;
    }

    
}