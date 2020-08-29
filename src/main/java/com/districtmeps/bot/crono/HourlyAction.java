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

package com.districtmeps.bot.crono;

import java.time.LocalDateTime;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.Main;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

/**
 * HourlyAction
 */
public class HourlyAction implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
       
        

        String extra = "\n" + getSpaces(20) + "Version " + Main.gitVersion + " - " + Main.gitComment;

        int hour = LocalDateTime.now().getHour();

        for (JDA jda : Main.getShards()) {

            switch (hour % 4) {
            case 0:
                jda.getShardManager().setPresence(OnlineStatus.DO_NOT_DISTURB,
                        Activity.watching("tv || " + Constants.PREFIX + "help" + extra));
                break;
            case 1:
                jda.getShardManager().setPresence(OnlineStatus.DO_NOT_DISTURB,
                        Activity.playing("at the park || " + Constants.PREFIX + "help" + extra));
                break;
            case 2:
                jda.getShardManager().setPresence(OnlineStatus.DO_NOT_DISTURB,
                        Activity.watching("District meps || " + Constants.PREFIX + "help" + extra));
                break;
            case 3:
                jda.getShardManager().setPresence(OnlineStatus.DO_NOT_DISTURB,
                        Activity.listening("to smooth beats || " + Constants.PREFIX + "help" + extra));
                break;
            }
        }

    }

    private String getSpaces(int num){
        String spaces = "";

        for (int i = 0; i < num; i++) {
            spaces += "\u3000";
        }

        return spaces;
    }

}