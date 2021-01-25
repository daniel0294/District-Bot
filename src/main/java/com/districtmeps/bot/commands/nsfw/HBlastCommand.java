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

package com.districtmeps.bot.commands.nsfw;

import java.util.List;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.objects.ICommand;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * HBlastCommand
 */
public class HBlastCommand implements ICommand {
    private final int maxTimes = 10;


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        int times = 0;

        if (!event.getChannel().isNSFW()) {
            event.getChannel().sendMessage("Channel must be nsfw").queue();
            return;
        }

        if (args.isEmpty()) {
            times = 5;
        } else {
            if (args.size() != 1) {
                event.getChannel()
                        .sendMessage("Hmm not right, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
                return;
            }

            try {
                times = Integer.parseInt(args.get(0));
            } catch (Exception e) {
                event.getChannel().sendMessage("Must only be a number").queue();
                return;
            }

            if(times > maxTimes){
                event.getChannel().sendMessage("Number can not be more than `" + maxTimes + "`").queue();
                return;
            }
        }

       

        

        for (int i = 0; i < times; i++) {
            HentaiCommand command = new HentaiCommand();
            command.handle(args, event);
        }

    }

    @Override
    public String getHelp() {
        return "Send multiple images of hentai\nUsage: `" + Constants.PREFIX + getInvoke()
                + " [NUM]` \n(default is 5 | | Max is " + maxTimes + ")";
    }

    @Override
    public String getInvoke() {
        return "hblast";
    }

    @Override
    public int getType() {
        return 6;
    }

}