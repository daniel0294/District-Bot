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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.List;

import javax.imageio.ImageIO;

import com.districtmeps.bot.objects.ICommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UploadHelpCommand implements ICommand {
    private final Logger logger = LoggerFactory.getLogger(UploadHelpCommand.class);

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        List<Attachment> att = event.getMessage().getAttachments();

        if (att.size() != 1) {
            event.getChannel().sendMessage("Please upload only one photo").queue();
            return;
        }

        if (args.size() < 1) {
            event.getChannel().sendMessage("Please also send the link").queue();
            return;
        }

       if(!checkLink(args.get(0))){
            event.getChannel().sendMessage("Please send a valid link").queue();
            return;
       }

        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
        builder.setFooter(String.format("Image posted by %#s", event.getAuthor()));
        builder.setTimestamp(Instant.now());
        builder.setDescription("[Sauce](" + args.get(0) + ")");
        // builder.setImage(att.get(0).getProxyUrl());
        
        if(args.size() > 1){

            String text = "";
            
            if(args.size() == 2){
                text = args.get(1);
            } else {

                text = reformatListToText(args);
            }

            builder.appendDescription("\n" + text);
        }
        


        final BufferedImage image;
        try {
            URL url = new URL(att.get(0).getProxyUrl());

            URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", WebUtils.getUserAgent());

            // String contentType = urlConn.getContentType();

            // System.out.println("contentType:" + contentType);

            InputStream is = urlConn.getInputStream();
            image = ImageIO.read(is);

            File fileImg = new File("image.jpg");
            ImageIO.write(image, "jpg", fileImg);

            // System.out.println(Files.size(fileImg.toPath()));

            builder.setImage("attachment://image.jpg");
            // builder.setColor(averageColor(image, image.getWidth(), image.getHeight()));

            // event.getChannel().sendMessage(builder.build()).queue();
            event.getChannel().sendFile(fileImg, "image.jpg").embed(builder.build()).queue((m) -> {
                builder.setColor(averageColor(image, image.getWidth(), image.getHeight()));
                m.editMessage(builder.build()).queue();
            });

            fileImg.delete();

        } catch (IOException e) {
            logger.error("Image could not be read from link");
            builder.setColor(EmbedUtils.getDefaultColor());
            builder.setImage(att.get(0).getProxyUrl());

            event.getChannel().sendMessage(builder.build()).queue();
        } catch(IllegalArgumentException e){
            event.getChannel().sendMessage("File to large to send back. Max 8MiB").queue();
        }

        

        

        try{

            event.getMessage().delete().queue();
        } catch (Exception e){
            event.getChannel().sendMessage("I could not delete the original message.").queue();
        }
        
        // event.getChannel().sendMessage(att.get(0).getProxyUrl()).queue();


    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "upload";
    }

    @Override
    public int getType() {
        return 3;
    }

    private boolean checkLink(String link) {
        return link.startsWith("https://") || link.startsWith("http://");
    }

    private Color averageColor(BufferedImage bi, int w, int h) {

        int skip = 10;
        long sumr = 0, sumg = 0, sumb = 0;
        for (int x = 0; x < w; x += skip) {
            for (int y = 0; y < h; y += skip) {
                Color pixel = new Color(bi.getRGB(x, y));

                int red = pixel.getRed(), green = pixel.getGreen(), blue = pixel.getBlue();

                // if((red + green + blue) < 100){
                // continue;
                // }

                sumr += red;
                sumg += green;
                sumb += blue;
            }
        }
        int num = w * h;
        num /= (skip * skip);
        float red = sumr / num;
        float green = sumg / num;
        float blue = sumb / num;
        // System.out.println(red / 255 + " - " + green / 255 + " - " + blue / 255);

        return new Color(red / 255, green / 255, blue / 255);
    }

    private String reformatListToText(List<String> list){

        String text = "";

        for (int i = 1; i < list.size(); i++) {
            text += list.get(i) + " ";
        }

        return text;
    }

}
