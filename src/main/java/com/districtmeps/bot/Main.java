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

package com.districtmeps.bot;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import javax.security.auth.login.LoginException;

import com.districtmeps.bot.config.Config;
import com.districtmeps.bot.crono.MainScheduler;
import com.districtmeps.bot.objects.APIHelper;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import org.json.JSONException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Main {

    private final Random random = new Random();
    private static ShardManager sManager = null;
    private static EventWaiter waiter = new EventWaiter();

    public static String gitVersion;
    public static String gitComment;
    private Main() throws IOException {

        Config config = new Config(new File("botconfig.json"));
        

        CommandManager commandManager = new CommandManager(waiter);
        Listener listener = new Listener(commandManager);
        Logger logger = LoggerFactory.getLogger(Main.class);

        WebUtils.setUserAgent("Mozilla/5.0 District JDA Bot/Danboi#4961");
        EmbedUtils.setEmbedBuilder(() -> new EmbedBuilder().setColor(getRandomColor()).setFooter(Constants.NAME, null)
                .setTimestamp(Instant.now()));

        try {
            logger.info("Booting");

            DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.getString("token"));
            builder.setActivity(Activity.listening("smooth beats"));
            builder.addEventListeners(waiter, listener);
            builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS);
            sManager = builder.build();

            

            logger.info("Ready");
            logger.info("" + LocalDateTime.now().getHour());

        } catch (LoginException | JSONException e) {
            e.printStackTrace();
        }

        for(JDA jda: sManager.getShards()){
            
            jda.getShardManager().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.playing("at the park || " + Constants.PREFIX + "help"));
        }

        logger.info("Loading Prescence");
        String[] info = APIHelper.getGitInfo();
        gitVersion = info[0];
        gitComment = info[1];
        MainScheduler daily = new MainScheduler();
		try {
			daily.start();
		} catch (SchedulerException e) {
			
			e.printStackTrace(); 
        }
        logger.info("Loaded Prescence");

    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

    private Color getRandomColor(){
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        
        return new Color(r,g,b);
    }

    public static List<JDA> getShards(){
        return sManager.getShards();
    }

}
