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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.districtmeps.bot.commands.district.GetInstaCommand;
import com.districtmeps.bot.commands.district.LogoCommand;
import com.districtmeps.bot.commands.district.UpdateInstaComand;
import com.districtmeps.bot.commands.district.meps.CreateMepCommand;
import com.districtmeps.bot.commands.district.meps.GetMepsCommand;
import com.districtmeps.bot.commands.district.meps.MepInfoCommand;
import com.districtmeps.bot.commands.district.meps.PartsCommand;
import com.districtmeps.bot.commands.fun.CatCommand;
import com.districtmeps.bot.commands.fun.DogCommand;
import com.districtmeps.bot.commands.fun.EventWaiterExampleCommand;
import com.districtmeps.bot.commands.fun.KitsuneCommand;
import com.districtmeps.bot.commands.fun.MemeCommand;
import com.districtmeps.bot.commands.fun.NekoCommand;
import com.districtmeps.bot.commands.fun.NekoGifCommand;
import com.districtmeps.bot.commands.fun.OWOCommand;
import com.districtmeps.bot.commands.holo.HoloCommand;
import com.districtmeps.bot.commands.holo.HoloEroCommand;
import com.districtmeps.bot.commands.holo.HoloLewdCommand;
import com.districtmeps.bot.commands.mod.BanCommand;
import com.districtmeps.bot.commands.mod.KickCommand;
import com.districtmeps.bot.commands.mod.PreviewDMCommand;
import com.districtmeps.bot.commands.mod.PurgeCommand;
import com.districtmeps.bot.commands.mod.SendDMCommand;
import com.districtmeps.bot.commands.mod.UnbanCommand;
import com.districtmeps.bot.commands.money.CoinFlipCommand;
import com.districtmeps.bot.commands.money.CoinsCommand;
import com.districtmeps.bot.commands.money.DailyCoinsCommand;
import com.districtmeps.bot.commands.money.PayCommand;
import com.districtmeps.bot.commands.music.JoinCommand;
import com.districtmeps.bot.commands.music.LeaveCommand;
import com.districtmeps.bot.commands.music.NowPlayingCommand;
import com.districtmeps.bot.commands.music.PauseCommand;
import com.districtmeps.bot.commands.music.PlayCommand;
import com.districtmeps.bot.commands.music.QueueCommand;
import com.districtmeps.bot.commands.music.ShuffleCommand;
import com.districtmeps.bot.commands.music.SkipCommand;
import com.districtmeps.bot.commands.music.StopCommand;
import com.districtmeps.bot.commands.music.VolumeCommand;
import com.districtmeps.bot.commands.music.YTSearchCommand;
import com.districtmeps.bot.commands.normal.GitHubCommand;
import com.districtmeps.bot.commands.normal.HelpCommand;
import com.districtmeps.bot.commands.normal.PingCommand;
import com.districtmeps.bot.commands.normal.ProfileCommand;
import com.districtmeps.bot.commands.normal.ServerInfoCommand;
import com.districtmeps.bot.commands.normal.UptimeCommand;
import com.districtmeps.bot.commands.nsfw.FutaCommand;
import com.districtmeps.bot.commands.nsfw.HBlastCommand;
import com.districtmeps.bot.commands.nsfw.HNekoCommand;
import com.districtmeps.bot.commands.nsfw.HentaiCommand;
import com.districtmeps.bot.commands.nsfw.PussyCommand;
import com.districtmeps.bot.commands.nsfw.TrapCommand;
import com.districtmeps.bot.commands.owner.EvalCommand;
import com.districtmeps.bot.commands.owner.ShardInfoCommand;
import com.districtmeps.bot.config.Config;
import com.districtmeps.bot.objects.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * CommandManager
 */
public class CommandManager {

    private final Map<String, ICommand> commands = new HashMap<>();

    CommandManager(EventWaiter waiter) {
        if (Config.getInstance().getBoolean("loadcommands")) {
            System.out.println("Loading Commands");

            // Owner 1
            addCommand(new EvalCommand());
            addCommand(new ShardInfoCommand());

            // Moderation 2
            addCommand(new KickCommand());
            addCommand(new BanCommand());
            addCommand(new UnbanCommand());
            addCommand(new PurgeCommand());
            addCommand(new SendDMCommand());
            addCommand(new PreviewDMCommand());

            // Normal 3
            addCommand(new PingCommand());
            addCommand(new HelpCommand(this));
            addCommand(new ProfileCommand());
            addCommand(new ServerInfoCommand());
            addCommand(new UptimeCommand());
            addCommand(new GitHubCommand());

            // Music 4
            addCommand(new JoinCommand());
            addCommand(new LeaveCommand());
            addCommand(new PlayCommand());
            addCommand(new StopCommand());
            addCommand(new QueueCommand());
            addCommand(new SkipCommand());
            addCommand(new NowPlayingCommand());
            addCommand(new YTSearchCommand(waiter));
            addCommand(new VolumeCommand());
            addCommand(new ShuffleCommand());
            addCommand(new PauseCommand());

            // Fun 5
            addCommand(new CatCommand());
            addCommand(new DogCommand());
            addCommand(new MemeCommand());
            addCommand(new EventWaiterExampleCommand(waiter));
            addCommand(new OWOCommand());
            addCommand(new NekoGifCommand());
            addCommand(new KitsuneCommand());
            addCommand(new NekoCommand());

            // NSFW 6
            addCommand(new HNekoCommand());
            addCommand(new TrapCommand());
            addCommand(new PussyCommand());
            addCommand(new FutaCommand());
            addCommand(new HBlastCommand());
            addCommand(new HentaiCommand());

            // Holo 7
            addCommand(new HoloLewdCommand());
            addCommand(new HoloCommand());
            addCommand(new HoloEroCommand());

            // Money 8
            addCommand(new CoinsCommand());
            addCommand(new CoinFlipCommand());
            addCommand(new PayCommand());
            addCommand(new DailyCoinsCommand());

            // District 9
            addCommand(new LogoCommand());
            addCommand(new UpdateInstaComand());
            addCommand(new GetInstaCommand());
            // addCommand(new CreateMepCommand(waiter));
            // addCommand(new GetMepsCommand());
            // addCommand(new MepInfoCommand());
            // addCommand(new PartsCommand()); 

        }
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }



    // public Collection<ICommand> getCommands(int type){
    //     Map<String, ICommand> newCommands = new HashMap<>();

    //     commands.forEach((k, v)->{
    //         if(v.getType() == type){
    //             newCommands.put(k, v);
    //         }
    //     });

    //     return newCommands.values();
    // }

    public List<ICommand> getCommands(int type){
        List<ICommand> newCommands = new ArrayList<>();

        commands.forEach((k, v)->{
            if(v.getType() == type){
                newCommands.add(v);
            }
        });

        return newCommands;
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }

    public int getType(ICommand command){
        return command.getType();
    }

    void handleCommand(GuildMessageReceivedEvent event) {
        
        final String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Constants.PREFIX), "").split("\\s+");

        // for(String text: split){
        //     System.out.println(text);
        // }
        
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            event.getChannel().sendTyping().queue();
            commands.get(invoke).handle(args, event);

        }
    }
}