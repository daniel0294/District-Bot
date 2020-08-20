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

package com.districtmeps.bot.commands.district.meps;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.districtmeps.bot.objects.APIHelper;
import com.districtmeps.bot.objects.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * CreateMepCommand
 */
public class CreateMepCommand implements ICommand {

    private EventWaiter waiter;
    private String mepName;
    private int parts;
    private String descM;
    private String videoLink;
    private int mepId;

    public CreateMepCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        //Dist and Test
        if(!event.getGuild().getId().equals("296844096813793281") && !event.getGuild().getId().equals("374386835327287306")){
            event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
            return;
        }

        //Just Dist
        // if(!event.getGuild().getId().equals("296844096813793281")){
        //     event.getChannel().sendMessage("This command can only be used in the District Discord Server").queue();
        //     return;
        // }

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();

        event.getChannel().sendMessage(embed.build()).queue((bEmbed) -> {

            event.getChannel().sendMessage("Alrighty! Lets set up your MEP. First, what do you want to name the MEP?"
                    + "\nYou can type `cancel` at any time to cancel the creation\n\n`Please type the name of the MEP`\n`You can change this later`")
                    .queue((message) -> {

                        waiter.waitForEvent(GuildMessageReceivedEvent.class, (msg) -> {
                            return event.getAuthor().equals(msg.getAuthor());
                        }, (msg) -> {

                            mepName = msg.getMessage().getContentRaw();

                            if (mepName.equalsIgnoreCase("cancel")) {
                                msg.getChannel().sendMessage("MEP Creation has been cancelled").queue();
                                bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();
                                message.delete().queue();
                                return;
                            }

                            embed.setTitle("MEP Info").addField("MEP Name", mepName, false).setTimestamp(Instant.now());
                            bEmbed.editMessage(embed.build()).queue();
                            bEmbed.editMessage("Temporary MEP Info").queue();
                            message.delete().queue();

                            Message partMessage = event.getChannel().sendMessage("Cool! The MEP will be named `"
                                    + mepName + "`. Next, how many parts will there be in the MEP?"
                                    + "\n\n`Please enter the number # of parts that will be in this MEP.`\n`You can change this later`")
                                    .complete();

                            waiter.waitForEvent(GuildMessageReceivedEvent.class, (msgPart) -> {
                                return event.getAuthor().equals(msgPart.getAuthor());
                            }, (msgPart) -> {
                                if (msgPart.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
                                    msgPart.getChannel().sendMessage("MEP Creation has been cancelled").queue();
                                    bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();

                                    partMessage.delete().queue();
                                    return;
                                }

                                String test = msgPart.getMessage().getContentRaw();
                                try {
                                    parts = Integer.parseInt(test);
                                    embed.setTitle("MEP Info").addField("MEP Parts", test, true)
                                            .setTimestamp(Instant.now());
                                    bEmbed.editMessage(embed.build()).queue();
                                    partMessage.delete().queue();
                                } catch (Exception e) {
                                    partMessage.delete().queue();
                                    event.getChannel().sendMessage(
                                            "Sorry but something went wrong\n\n`Hint: you must only enter a number`\nPlease restart and try again")
                                            .queue();

                                    return;

                                }

                                Message linkMessage = event.getChannel().sendMessage("Alright! Your mep will have `"
                                        + parts + "` parts that members can choose from.\n"
                                        + "Now let's add a video link/file link where the parts are.\n\n`Please send a link that contains the parts for the MEP`\n`You can change this later`")
                                        .complete();

                                waiter.waitForEvent(GuildMessageReceivedEvent.class, link -> {
                                    return event.getAuthor().equals(link.getAuthor());
                                }, link -> {
                                    if (link.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
                                        link.getChannel().sendMessage("MEP Creation has been cancelled").queue();
                                        bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();
                                        linkMessage.delete().queue();
                                        return;
                                    }

                                    videoLink = link.getMessage().getContentRaw();
                                    embed.setTitle("MEP Info").addField("MEP Link", videoLink, true)
                                            .setTimestamp(Instant.now());
                                    bEmbed.editMessage(embed.build()).queue();
                                    linkMessage.delete().queue();

                                    Message descMessage = event.getChannel()
                                            .sendMessage("Next! Lets add a description for your MEP "
                                                    + "\n\n`Please type a description for the MEP.`\n`It is recommended that you have this pre typed.`\n`You can change this later`")
                                            .complete();
                                    waiter.waitForEvent(GuildMessageReceivedEvent.class, desc -> {
                                        return event.getAuthor().equals(desc.getAuthor());
                                    }, desc -> {
                                        if (desc.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
                                            desc.getChannel().sendMessage("MEP Creation has been cancelled").queue();
                                            bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();
                                            descMessage.delete().queue();
                                            return;
                                        }

                                        descM = desc.getMessage().getContentRaw();
                                        embed.setTitle("MEP Info").addField("MEP Description", descM, false)
                                                .setTimestamp(Instant.now());
                                        bEmbed.editMessage(embed.build()).queue();
                                        descMessage.delete().queue();

                                        // Test event.getChannel().sendMessage(mepName + " | " + parts + " | " +
                                        // videoLink + " | " + descM).queue();

                                        bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();
                                        
                                        embed.setTitle("Information for Mep #`id tbd` hosted by " + event.getAuthor().getName()).setTimestamp(Instant.now());
                                        Message finalMessage = event.getChannel().sendMessage(embed.build()).complete();
                                        
                                        // event.getChannel()
                                        //         .sendMessage(String.format("https://discordapp.com/channels/%s/%s/%s",
                                        //                 finalMessage.getGuild().getId(),
                                        //                 finalMessage.getChannel().getId(), finalMessage.getId()))
                                        //         .queue();
                                        
                                        
                                        embed.addField("Message Link",String.format("https://discordapp.com/channels/%s/%s/%s",
                                                        finalMessage.getGuild().getId(),
                                                        finalMessage.getChannel().getId(), finalMessage.getId()), false);

                                        finalMessage = finalMessage.editMessage(embed.build()).complete();
                                        try {
                                            finalMessage.getChannel().pinMessageById(finalMessage.getId()).queue();
                                        } catch (Exception e) {
                                            
                                            finalMessage.getChannel().sendMessage("Could not pin the embed to this channel").queue();
                                        }
                                        

                                        mepId = APIHelper.createMep(mepName, parts, videoLink, descM, finalMessage.getGuild().getId(), finalMessage.getChannel().getId() + "/" + finalMessage.getId());
                                        // event.getGuild().getId(), finalMessage.getId());
                                        embed.setTitle("Information for Mep #`" + mepId + "` hosted by " + event.getAuthor().getName()).setTimestamp(Instant.now());

                                        finalMessage = finalMessage.editMessage(embed.build()).complete();

                                    }, 60, TimeUnit.SECONDS, () -> {
                                        bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();
                                        event.getChannel().sendMessage("Mep Description Declaration Timed Out \n`60 seeconds`")
                                                .queue();
                                        descMessage.delete().queue();
                                    });
                                }, 60, TimeUnit.SECONDS, () -> {
                                    bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();
                                    event.getChannel().sendMessage("Mep Link Declaration Timed Out \n`60 seeconds`")
                                            .queue();
                                    linkMessage.delete().queue();
                                });
                            }, 60, TimeUnit.SECONDS, () -> {
                                bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();
                                event.getChannel().sendMessage("Mep Parts # Declaration Timed Out \n`60 seeconds`")
                                        .queue();
                                partMessage.delete().queue();
                            });
                        }, 60, TimeUnit.SECONDS, () -> {
                            bEmbed.getChannel().deleteMessageById(bEmbed.getId()).queue();
                            event.getChannel().sendMessage("Mep Naming Timed Out \n`60 seeconds`").queue();
                            message.delete().queue();
                        });

                    });
        });

    }

    @Override
    public String getHelp() {

        return "Allows you the start the process with the Bot the start a mep";
    }

    @Override
    public String getInvoke() {
        return "createmep";
    }

    @Override
    public int getType() {
        return 9;
    }

}