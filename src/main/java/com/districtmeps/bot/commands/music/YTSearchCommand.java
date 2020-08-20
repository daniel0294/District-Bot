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
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.districtmeps.bot.Constants;
import com.districtmeps.bot.config.Config;
import com.districtmeps.bot.music.PlayerManager;
import com.districtmeps.bot.objects.ICommand;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * YTSearchCommand
 */
public class YTSearchCommand implements ICommand {

    Logger logger = LoggerFactory.getLogger(PlayCommand.class);
    private final YouTube youTube;

    private EventWaiter waiter;

    public YTSearchCommand(EventWaiter waiter) {
        this.waiter = waiter;
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
        System.out.println(args + "\n" + input);

        if (!isUrl(input)) {

            @Nullable
            List<SearchResult> ytSearch = searchYoutube(input);

            if (ytSearch == null) {
                channel.sendMessage("Youtubed returned no results").queue();
                return;
            }

            EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("YouTube Search Results:");

            StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

            for (int i = 0; i < ytSearch.size(); i++) {
                SearchResult vid = ytSearch.get(i);
                descriptionBuilder.append((i + 1) + ") `")
                        .append(vid.getSnippet().getTitle() + " | | " + vid.getSnippet().getChannelTitle()
                                + "`\nhttps://www.youtube.com/watch?v=" + vid.getId().getVideoId())
                        .append("\n\n");

            }

            event.getChannel().sendMessage(builder.build()).queue((m) -> {
                event.getChannel().sendMessage("If you would like to add the song to the music playlist please do `"
                        + Constants.PREFIX + "select <NUM>`\nYou've got 10 seconds").queue();
                ShardManager shardManager = event.getJDA().getShardManager();
                waiter.waitForEvent(GuildMessageReceivedEvent.class, (recieved) -> {

                    return event.getAuthor().equals(recieved.getAuthor()) && checkNewCommand(recieved);
                }, (recieved) -> {
                    String[] split = recieved.getMessage().getContentRaw()
                            .replaceFirst("(?i)" + Pattern.quote(Constants.PREFIX), "").split("\\s+");
                    int num = Integer.parseInt(split[1]);
                    SearchResult vid = ytSearch.get(num - 1);

                    PlayerManager manager = PlayerManager.getInstance();

                    manager.loadAndPlay(event.getChannel(),
                            "https://www.youtube.com/watch?v=" + vid.getId().getVideoId());
                }, 10, TimeUnit.SECONDS, () -> {
                    TextChannel tchannel = shardManager.getTextChannelById(event.getChannel().getId());

                    tchannel.sendMessage("I stopped listening for a reaction").queue();
                });
            });

        }
    }

    private boolean checkNewCommand(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Constants.PREFIX), "")
                .split("\\s+");
        String rawMessage = event.getMessage().getContentRaw();

        if (!rawMessage.startsWith(Constants.PREFIX + "select")) {
            return false;
        }
        if (split.length != 2) {
            event.getChannel().sendMessage("Please only enter a number after the command .").queue();
            return false;
        }

        
        try {
            Integer.parseInt(split[1]);
        } catch (Exception e) {
            event.getChannel().sendMessage("Please only enter a number after the command.").queue();
            return false;
        }

        return true;
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
    private List<SearchResult> searchYoutube(String input) {

        long max = Constants.YTSEARCH;

        try {
            List<SearchResult> results = youTube.search().list("id,snippet").setQ(input).setMaxResults(max)
                    .setType("video")
                    .setFields(
                            "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/channelTitle)")
                    .setKey(Config.getInstance().getString("youtubekey")).execute().getItems();

            if (!results.isEmpty()) {

                return results;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getHelp() {
        return "Returns a list of " + Constants.YTSEARCH + " youtube videos results from your search\n" + "Usage: `"
                + Constants.PREFIX + getInvoke() + " <search terms>`";
    }

    @Override
    public String getInvoke() {
        return "ytsearch";
    }

    @Override
    public int getType() {
        return 4;
    }

}