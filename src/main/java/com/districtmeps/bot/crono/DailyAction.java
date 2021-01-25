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

package com.districtmeps.bot.crono;

import com.districtmeps.bot.Main;
import com.districtmeps.bot.objects.APIHelper;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.dv8tion.jda.api.JDA;

/**
 * DailyAction
 */
public class DailyAction implements Job{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        JDA jda = Main.getShards().get(0);

        

        jda.getGuildById("374386835327287306").getTextChannelById("375130022467600394").sendMessage(APIHelper.dailyReset()).queue();
    }
}