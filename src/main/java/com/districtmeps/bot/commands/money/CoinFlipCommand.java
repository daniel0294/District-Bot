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
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * CoinFlipCommand
 */
public class CoinFlipCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
        builder.setTitle("Results");
        int flipCoins;
        int userCoins;
        String flip;
        int flipInt;
        String message;

        if(args.size() != 2){
            event.getChannel().sendMessage("Something is not right, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }
        
        
        try {
            flipCoins = Integer.parseInt(args.get(0));
        } catch (Exception e) {
            event.getChannel().sendMessage("Something is not right, check `" + Constants.PREFIX + "help " + getInvoke() + "`\n\nHint: Amount of coins must be a number").queue();
            return;
        }

        if(flipCoins < 1){
            event.getChannel().sendMessage("You can't flip less than 1 coin").queue();
            return;
        }
        
        flip = args.get(1);
        if (!(flip.equalsIgnoreCase("heads") || flip.equalsIgnoreCase("tails") || flip.equalsIgnoreCase("t") || flip.equalsIgnoreCase("h"))) {
            event.getChannel().sendMessage("Something is not right, check `" + Constants.PREFIX + "help " + getInvoke() + "`\n\nHint: Flip guess must be heads/tails/h/t").queue();
            return;
        }

        userCoins = APIHelper.getCoins(event.getAuthor().getId());

        if (flipCoins > userCoins){
            builder.setDescription("You can't flip more coins that have.\nYou have `" + userCoins + "` coins. <a:coin:630157923141812245>");
            event.getChannel().sendMessage(builder.build()).queue();
            return;
        }

        if(flip.equalsIgnoreCase("heads") || flip.equalsIgnoreCase("h")){
            flipInt = 0;
        } else if(flip.equalsIgnoreCase("tails") || flip.equalsIgnoreCase("t")){
            flipInt = 1;
        } else {
            flipInt = -1;
        }

        message = APIHelper.coinFlip(event.getAuthor().getId(), flipCoins, flipInt);

        
        builder.setDescription(message);

        event.getChannel().sendMessage(builder.build()).queue();

    }

    @Override
    public String getHelp() {
        return "Flip heads or tails to double/lose your coins\n" + "Usage: `" + Constants.PREFIX + getInvoke()
                + " [# of coins] [h/t/heads/tails]`";
    }

    @Override
    public String getInvoke() {
        return "coinflip";
    }

    @Override
    public int getType() {
        return 8;
    }


}