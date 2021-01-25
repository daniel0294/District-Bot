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

package com.districtmeps.bot.commands.normal;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

import com.districtmeps.bot.objects.ICommand;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * UptimeCommand
 */
public class UptimeCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        long uptimeInSeconds = uptime / 1000;
        long numberOfHours = uptimeInSeconds / (60 * 60);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = uptimeInSeconds % 60;
        long numberOfDays = numberOfHours / 24;

        event.getChannel().sendMessageFormat("My Uptime is `%s days, %s hours, %s minutes, %s seconds`", numberOfDays,
                (numberOfHours % 24), numberOfMinutes, numberOfSeconds).queue();

    }

    @Override
    public String getHelp() {
        return "Shows the current uptime of the bot.";
    }

    @Override
    public String getInvoke() {
        return "uptime";
    }

    @Override
    public int getType() {
        return 3;
    }

}