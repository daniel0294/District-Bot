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

package com.districtmeps.bot.commands.music;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.config.Config;
import com.districtmeps.bot.music.PlayerManager;
import com.districtmeps.bot.objects.ICommand;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * PlayCommand
 */
public class PlayCommand implements ICommand {

    Logger logger = LoggerFactory.getLogger(PlayCommand.class);

    // private final String emote1Id = "624063957283504128";
    // private final String emote2Id = "624065240891392011";
    // private final String emote3Id = "624065251939450928";
    // private final String emote4Id = "624065262173421608";
    // private final String emote5Id = "624065272197939204";

    private final YouTube youTube;

    public PlayCommand() {
        YouTube temp = null;

        try {

            temp = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), null).setApplicationName("Dan Test JDA Bot").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        youTube = temp;

    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        TextChannel channel = event.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments, check `" + Constants.PREFIX + "help " + getInvoke() + "`").queue();
            return;
        }

        String input = String.join(" ", args);

        if (!isUrl(input)) {



            String ytSearch = searchYoutube(input);

            if (ytSearch == null) {
                channel.sendMessage("Youtubed returned no results").queue();
                return;
            }
            input = ytSearch;
        }

        PlayerManager manager = PlayerManager.getInstance();

        manager.loadAndPlay(event.getChannel(), input);

        

    }

    private boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    private String searchYoutube(String input) {

        try {
            List<SearchResult> results = youTube.search().list("id,snippet").setQ(input).setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.getInstance().getString("youtubekey")).execute().getItems();

            if (!results.isEmpty()) {
                return "https://www.youtube.com/watch?v=" + results.get(0).getId().getVideoId();
  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public String getHelp() {
        return "Plays a song\n" + "Usage: `" + Constants.PREFIX + getInvoke() + " <song URL>`";
    }

    @Override
    public String getInvoke() {
        return "play";
    }

    @Override
    public int getType() {
        return 4;
    }

}