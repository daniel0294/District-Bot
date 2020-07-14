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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.districtmeps.bot.Main;
import com.districtmeps.bot.config.Config;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message.Attachment;
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
    private static String coinFlipMessage = "";
    private static Boolean payMessage = false;
    private static String dailyCoinsMessage = "";
    private static int mepId = 0;
    private static List<Map<String, String>> meps = null;
    private static Map<String, String> mep = null;
    private static Map<String, Object> parts = null;

    public static Integer getCoins(String id) {

        coins = null;
        String[] params = { "user_id=" + id };
        WebUtils.ins.getJSONObject(buildGETURL("discord_user", params)).async((json) -> {
            // JsonNode jsonUrl = json.get("count");
            // String count = jsonUrl.toString();

            // if (Integer.parseInt(count) < 1) {
            // sync.doNotify();
            // System.out.println("person not in system");
            // return;
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

        String messageId = event.getMessage().getId();
        String channelId = event.getChannel().getId();
        String server = event.getGuild().getName();
        String serverId =  event.getGuild().getId();


        String[] params = { 
            "user_id=" + userId, 
            "user_name=" + name, 
            "user_pfp=" + pfp,
            "message_id=" + messageId,
            "channel_id=" + channelId,
            "server_name=" + server,
            "server_id=" + serverId};
        WebUtils.ins.getJSONObject(buildGETURL("user_has_spoken_v2", params)).async((json) -> {

            // JsonNode jsonUrl = json.get("error");
            // String error = jsonUrl.toString();
            // System.out.println("test");

            JsonNode jsonUrl = json.get("message");
            String message = jsonUrl.toString();
            // System.out.println(message);

            if (!message.equals("null")) {
                logger.info(message);
            }

            // if (error != null) {
            // ErrorHelper.MessageAdmin(event.getJDA().getUserById(Config.getInstance().getString("owner")),
            // "APIHelper.java>APIHelper>UserHasSpoken(GuildMessageReceivedEvent event)",
            // error);
            // }

            // System.out.println("ETSTETST");

        });
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

    public static String coinFlip(String id, int coins, int flip) {
        coinFlipMessage = "test";
        String[] params = { "user_id=" + id, "coins=" + coins, "flip=" + flip };
        WebUtils.ins.getJSONObject(buildGETURL("coinflip", params)).async((json) -> {

            JsonNode jsonUrl = json.get("won");
            String won = jsonUrl.toString().substring(1, jsonUrl.toString().length() - 1);

            jsonUrl = json.get("coins");
            int newCoins = Integer.parseInt(jsonUrl.toString());

            jsonUrl = json.get("won_coins");
            int wonCoins = Integer.parseInt(jsonUrl.toString().substring(1, jsonUrl.toString().length() - 1));

            if (won.equals("yes")) {
                coinFlipMessage = "Congrats you won `" + wonCoins + "`\nYou now have `" + newCoins + "` coins.";
            } else if (won.equals("no")) {
                coinFlipMessage = "Oops you lost `" + wonCoins + "`\nYou now have `" + newCoins + "` coins.";
            } else {
                coinFlipMessage = "yoo wtf";
            }

            sync.doNotify();
        });
        sync.doWait();
        return coinFlipMessage;
    }

    // This is just a mess
    public static boolean payCoins(String payerId, String receiverId, int amt) {
        payMessage = false;

        String[] params = { "user_id=" + payerId, "receiver_id=" + receiverId, "coins=" + amt };
        WebUtils.ins.getJSONObject(buildGETURL("pay_coins", params)).async((json) -> {

            // JsonNode jsonUrl = json.get("message");
            // String message = jsonUrl.toString();

            payMessage = true;

            sync.doNotify();
        });
        sync.doWait();
        return payMessage;
    }

    public static String dailyCoins(String id) {

        dailyCoinsMessage = "";

        String[] params = { "user_id=" + id };
        WebUtils.ins.getJSONObject(buildGETURL("daily_coins", params)).async((json) -> {

            JsonNode jsonUrl = json.get("message");
            String message = jsonUrl.toString();

            dailyCoinsMessage = message.substring(1, message.length() - 1);

            sync.doNotify();
        });
        sync.doWait();
        return dailyCoinsMessage;
    }

    public static int createMep(String mepName, int parts, String videoLink, String desc, String server,
            String messageId) {
        mepId = 0;

        String[] params = { "mep_name=" + mepName, "parts=" + parts, "link=" + videoLink, "desc=" + desc,
                "server=" + server, "message_id=" + messageId };
        WebUtils.ins.getJSONObject(buildGETURL("create_mep", params)).async((json) -> {
            JsonNode jsonUrl = json.get("mep_id");
            String message = jsonUrl.toString();
            mepId = Integer.parseInt(message.substring(1, message.length() - 1));

            sync.doNotify();
        });
        sync.doWait();
        return mepId;
    }

    public static List<Map<String, String>> getMeps() {
        meps = new ArrayList<Map<String, String>>();
        WebUtils.ins.getJSONArray(buildGETURL("get_meps", null)).async((json) -> {

            int size = json.size();

            for (int i = 0; i < size; i++) {
                JsonNode node = json.get(i);
                // System.out.println("Test test look" + node.get("id").toString());

                Map<String, String> mep = new HashMap<>();

                String id = node.get("id").toString();
                String server = node.get("server").toString();
                String name = node.get("name").toString();
                String parts = node.get("parts").toString();
                String link = node.get("video_link").toString();
                String desc = node.get("description").toString();
                String messageId = node.get("message_id").toString();
                String completed = node.get("completed").toString();

                id = fixString(id);
                server = fixString(server);
                name = fixString(name);
                parts = fixString(parts);
                link = fixString(link);
                desc = fixString(desc);
                messageId = fixString(messageId);
                completed = fixString(completed);

                mep.put("id", id);
                mep.put("server", server);
                mep.put("name", name);
                mep.put("parts", parts);
                mep.put("link", link);
                mep.put("desc", desc);
                mep.put("messageId", messageId);
                mep.put("completed", completed);

                meps.add(mep);
            }

            sync.doNotify();
        });
        sync.doWait();
        return meps;

    }

    public static Map<String, String> getMep(int mepId) {

        mep = new HashMap<String, String>();
        String[] params = { "mep_id=" + mepId };
        WebUtils.ins.getJSONObject(buildGETURL("get_mep", params)).async((json) -> {

            
            if(json.get("error").asText().equals("true")){
                mep.put("error", "true");
                mep.put("message", json.get("message").asText());

                sync.doNotify();
                return;
            }

            JsonNode node = json.get("mep");
            // System.out.println(node.get("server").asText());


            mep.put("error", "false");
            mep.put("id", node.get("id").asText());
            mep.put("server", node.get("server").asText());
            mep.put("name", node.get("name").asText());
            mep.put("parts", node.get("parts").asText());
            mep.put("openParts", node.get("open_parts").asText());
            mep.put("doneParts", node.get("done_parts").asText());
            mep.put("videoLink", node.get("video_link").asText());
            mep.put("desc", node.get("description").asText());
            mep.put("messageId", node.get("message_id").asText());
            mep.put("completed", node.get("completed").asText());

            // mep.forEach((k, v) ->{
            //     System.out.println(k + " || " + v);
            // });

            sync.doNotify();
        });
        sync.doWait();
        return mep;
    }

    public static Map<String, Object> getParts(int mepId){
        SyncronizeObj sync1 = new SyncronizeObj();

        parts = new HashMap<String, Object>();

        String[] params = { "mep_id=" + mepId };
        WebUtils.ins.getJSONObject(buildGETURL("get_parts", params)).async((json) -> {

            if(json.get("error").asText().equals("true")){
                parts.put("error", "true");
                parts.put("message", json.get("message").asText());

                sync.doNotify();
                return;
            }

            parts.put("error", "false");
            


            Map<String, String> partsMep = APIHelper.getMep(mepId);
            int partsCount = Integer.parseInt(partsMep.get("parts"));
            parts.put("mepName", partsMep.get("name"));
            parts.put("count", partsMep.get("parts"));


            JsonNode node = json.get("parts");

            for(int i = 1; i <= partsCount; i++){
                Map<String, String> part = new HashMap<>();
                JsonNode partAPI = node.get("" + i);
                if(partAPI.asText() == "null"){
                    part = null;
                    
                } else {

                    // logger.info(node.get("" + i).get("user_id").asText());

                    part.put("userId", partAPI.get("user_id").asText());
                    part.put("mepId", partAPI.get("mep_id").asText());
                    part.put("part", partAPI.get("part").asText());
                    part.put("videoLink", partAPI.get("video_link").asText());
                    part.put("done", partAPI.get("done").asText());
                    

                }
                parts.put(i + "", part);
                

            }




            sync1.doNotify();
        });
        sync1.doWait();
        return parts;
    }

    public static Map<String, String> getInstaInfo(String id) {

        Map<String, String> instaInfo = new HashMap<>();
        String[] params = { "user_id=" + id };
        WebUtils.ins.getJSONObject(buildGETURL("discord_user", params)).async((json) -> {

            JsonNode jsonUrl = json.get("user");

            instaInfo.put("insta", jsonUrl.get("instagram").asText());
            instaInfo.put("messageId", jsonUrl.get("insta_message_id").asText());


            // System.out.println(jsonUrl.get("instagram").asText() +"\n" + jsonUrl.get("insta_message_id").asText() );
            sync.doNotify();

        });
        sync.doWait();
        return instaInfo;

    }

    public static void saveInsta(String id, String instaName, String messageId, GuildMessageReceivedEvent event) {

        try {
            String[] params = { 
                "user_id=" + id,
                "insta_name=" + instaName,
                "message_id=" + messageId };
            WebUtils.ins.getJSONObject(buildGETURL("save_insta", params)).async((json) -> {


                JsonNode jsonUrl = json.get("error");



                sync.doNotify();

            });
            sync.doWait();
            return;

        } catch (Exception e) {
            String mes = e.toString();
            event.getJDA().getUserById("151474961825923072").openPrivateChannel().queue((c)->{
                c.sendMessage("Insta Save Failed").queue();
                if (mes.length() > 1999) {
                    c.sendMessage(mes.substring(0,1998));
                } else {
                    c.sendMessage(mes).queue();
                }
                
            });
        }

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

    private static String fixString(String input) {
        return input.substring(1, input.length() - 1);
    }

}