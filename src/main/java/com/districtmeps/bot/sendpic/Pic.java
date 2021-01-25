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

package com.districtmeps.bot.sendpic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class Pic extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    BufferedImage img = null;
    GuildMemberJoinEvent event;

    public Pic(GuildMemberJoinEvent event) {
        this.event = event;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            img = ImageIO.read(new File("./media/welcm.png"));
            

        } catch (IOException e) {
        }
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        g.drawImage(img, 0, 0, (int) img.getWidth() / 2, (int) img.getHeight() / 2, null);
        int width = (int) img.getWidth() / 2;
        int height = (int) img.getHeight() / 2;

        URL url = null;
        BufferedImage imgimg = null;
        boolean defaultImg = false;
        try {

            if (event.getUser().getAvatarUrl() != null) {
                url = new URL(event.getUser().getAvatarUrl());
            } else {
                url = new URL(event.getUser().getDefaultAvatarUrl());
                defaultImg = true;
            }

            URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Chrome");

            String contentType = urlConn.getContentType();

            System.out.println("contentType:" + contentType);

            InputStream is = urlConn.getInputStream();
            imgimg = ImageIO.read(is);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (!defaultImg) {
            g.drawImage(imgimg, width / 2 - 31, height / 2 - 39, (int) (imgimg.getWidth() * 0.77),
                    (int) (imgimg.getHeight() * 0.77), null);
        } else {
            g.drawImage(imgimg, width / 2 - 31, height / 2 - 39, (int) (imgimg.getWidth() * (.5 * 0.77)),
                    (int) (imgimg.getHeight() * (.5 * 0.77)), null);
        }

        g.setColor(Color.WHITE);
        Font font = new Font("Leixo-Regular", 1, 60);
        String text = event.getUser().getName() + "#" + event.getUser().getDiscriminator();
        Rectangle2D bounds = font.getStringBounds(text, new FontRenderContext(null, false, false));
        g.setFont(font);

        System.out.println(font.getFontName());


        g.drawString(text, (int) (width / 2 - bounds.getWidth() / 2) + 20,
                (int) (height / 2 - bounds.getHeight() / 2) + 180);

    }
}
