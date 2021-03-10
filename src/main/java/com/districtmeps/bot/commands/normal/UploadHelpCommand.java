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

import java.time.Instant;
import java.util.List;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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

        String texts = "";
        for (String text : args) {
            if (!checkLink(text)) {
                event.getChannel().sendMessage("Please only send valid links").queue();
                return;
            }

            texts += (texts + "\n");
        }

        EmbedBuilder builder = EmbedUtils.getDefaultEmbed();
        builder.setFooter(String.format("Image posted by %#s", event.getAuthor()));
        builder.setTimestamp(Instant.now());
        builder.setDescription("[Sauce](" + args.get(0) + ")");
        builder.setImage(att.get(0).getProxyUrl());


        BufferedImage image = null;
        try {
            URL url = new URL(att.get(0).getProxyUrl());

            URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", WebUtils.getUserAgent());

            // String contentType = urlConn.getContentType();

            // System.out.println("contentType:" + contentType);

            InputStream is = urlConn.getInputStream();
            image = ImageIO.read(is);

            builder.setColor(averageColor(image, image.getWidth(), image.getHeight()));
        } catch (IOException e) {
            logger.error("Image could not be read from link");
            builder.setColor(EmbedUtils.getDefaultColor());
        }

        

        event.getChannel().sendMessage(builder.build()).queue();

        try{

            event.getMessage().delete().queue();
        } catch (Exception e){
            event.getChannel().sendMessage("I could not delete the original message.").queue();
        }
        
        // event.getChannel().sendMessage(att.get(0).getProxyUrl()).queue();

        logger.info(texts);

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


    public static Color averageColor(BufferedImage bi, int w,
        int h) {
    long sumr = 0, sumg = 0, sumb = 0;
    for (int x = 0; x < w; x++) {
        for (int y = 0; y < h; y++) {
            Color pixel = new Color(bi.getRGB(x, y));

            int red = pixel.getRed(), green = pixel.getGreen(), blue = pixel.getBlue();
            
            
            // if((red + green + blue) < 100){
            //     continue;
            // }

            sumr += red;
            sumg += green;
            sumb += blue;
        }
    }
    int num = w * h;
    float red = sumr / num;
    float green = sumg / num;
    float blue = sumb / num;
    // System.out.println(red / 255 + " - " + green / 255 + " - " + blue / 255);

    return new Color(red / 255, green / 255, blue / 255);
}

}
