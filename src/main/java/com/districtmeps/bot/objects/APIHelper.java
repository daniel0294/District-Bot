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

package com.districtmeps.bot.objects;

import com.districtmeps.bot.config.Config;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * APIHelper
 */
public class APIHelper {
    private final static Logger logger = LoggerFactory.getLogger(APIHelper.class);

    private static final String NAME = Config.getInstance().getString("dname");
    private static final String TOKEN = Config.getInstance().getString("dtoken");
    private static final String DURL = "https://districtmeps.com/api:v2/";
    private static SyncronizeObj sync = new SyncronizeObj();

    private static Integer coins = null;
    private static String dailyMessage = "";

    public static Integer getCoins(String id) {

        coins = null;
        String[] params = { "user_id=" + id };
        WebUtils.ins.getJSONObject(buildGETURL("discord_user", params)).async((json) -> {
            // JsonNode jsonUrl = json.get("count");
            // String count = jsonUrl.toString();

            // if (Integer.parseInt(count) < 1) {
            //     sync.doNotify();
            //     System.out.println("person not in system");
            //     return;
            // }
            JsonNode jsonUrl = json.get("user").get("coins");
            String webCoins = jsonUrl.toString();
            webCoins = webCoins.substring(1, webCoins.length() - 1);
            coins = Integer.parseInt(webCoins);

            sync.doNotify();

        });
        sync.doWait();
        return coins;

    }

    public static void UserHasSpoken(GuildMessageReceivedEvent event) {

        Member member = event.getMember();
        String userId = event.getAuthor().getId();
        String name = member.getUser().getName();
        String pfp = member.getUser().getAvatarUrl();
        if (pfp == null) {
            pfp = member.getUser().getDefaultAvatarUrl();
        }

        String[] params = { "user_id=" + userId, "user_name=" + name, "user_pfp=" + pfp };
        WebUtils.ins.getJSONObject(buildGETURL("user_has_spoken", params)).async((json) -> {

            // JsonNode jsonUrl = json.get("error");
            // String error = jsonUrl.toString();
            // System.out.println("test");

            JsonNode jsonUrl = json.get("message");
            String message = jsonUrl.toString();
            // System.out.println(message);

            if (message != null) {
                logger.info(message);
            }

            // if (error != null) {
            //     ErrorHelper.MessageAdmin(event.getJDA().getUserById(Config.getInstance().getString("owner")),
            //             "APIHelper.java>APIHelper>UserHasSpoken(GuildMessageReceivedEvent event)", error);
            // }

            sync.doNotify();

        });
        sync.doWait();
        return;
    }

    public static String dailyReset() {

        dailyMessage = "Daily Reset bot side done";
        WebUtils.ins.getJSONObject(buildGETURL("daily_reset", null)).async((json) -> {
            JsonNode jsonUrl = json.get("message");
            String webMessage = jsonUrl.toString();

            dailyMessage += " | | " + webMessage;

            sync.doNotify();

        });
        sync.doWait();

        return dailyMessage;
    }

    private static String buildGETURL(String uri, String[] params) {

        String url = DURL + uri + "?name=" + NAME + "&token=" + TOKEN;

        if (params != null) {
            for (String p : params) {
                url += ("&" + p);
            }
        }

        // System.out.println(url);
        return url;

    }
}